package com.MovieDb.app.core.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.MovieDb.app.core.data.source.remote.network.ApiResponse;
import com.MovieDb.app.core.utils.AppExecutors;

public abstract class NetworkBoundResource<ResultType, RequestType> {

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();
    private final AppExecutors mExecutors;

    public NetworkBoundResource(AppExecutors appExecutors) {
        this.mExecutors = appExecutors;
        result.setValue(Resource.loading(null));

        LiveData<ResultType> dbSource = loadFromDB();

        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource);
            } else {
                result.addSource(dbSource, newData -> result.setValue(Resource.success(newData)));
            }
        });
    }

    @SuppressWarnings("EmptyMethod")
    protected void onFetchFailed() {
    }

    protected abstract LiveData<ResultType> loadFromDB();

    protected abstract Boolean shouldFetch(ResultType data);

    protected abstract LiveData<ApiResponse<RequestType>> createCall();

    protected abstract void saveCallResult(RequestType data);

    private void fetchFromNetwork(LiveData<ResultType> dbSource) {
        LiveData<ApiResponse<RequestType>> apiResponse = createCall();

        result.addSource(dbSource, newData ->
                result.setValue(Resource.loading(newData))
        );

        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            result.removeSource(dbSource);

            switch (response.getStatus()) {
                case SUCCESS:
                    mExecutors.diskIO().execute(() -> {
                        saveCallResult(response.getBody());

                        mExecutors.mainThread().execute(() ->
                                result.addSource(loadFromDB(),
                                        newData -> result.setValue(Resource.success(newData))));
                    });
                    break;

                case EMPTY:
                    mExecutors.mainThread().execute(() ->
                            result.addSource(loadFromDB(),
                                    newData -> result.setValue(Resource.success(newData))));
                    break;

                case ERROR:
                    onFetchFailed();
                    result.addSource(dbSource, newData ->
                            result.setValue(Resource.error(response.getMessage(), newData)));
                    break;
            }
        });
    }

    public LiveData<Resource<ResultType>> asLiveData() {
        return result;
    }
}