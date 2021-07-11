package io.study.rxjavav1.step1;

import io.reactivex.Observable;

public class HelloWorldRxJava {
	public static void main(String [] args){
		Observable<String> observable = Observable.just("Hello", "World", "RxJava");
		observable.subscribe(str -> System.out.println(str));
	}
}
