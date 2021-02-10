package fr.heavenmoon.core.common.utils.wrappers;

public class LambdaWrapper<T> {
    private T data;

    public LambdaWrapper() {
    }

    public LambdaWrapper(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}