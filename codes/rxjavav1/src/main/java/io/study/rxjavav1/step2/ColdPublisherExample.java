package io.study.rxjavav1.step2;

import io.reactivex.Flowable;

public class ColdPublisherExample {
	public static void main(String [] args){
		Flowable<String> flowable = Flowable.just("방금", "밥 먹었잖아", "또 먹어?", "후식이라고?", "헐...");

		flowable.subscribe(message -> System.out.println("[구독자 1] " + message));
		flowable.subscribe(message -> System.out.println("[구독자 2] " + message));
	}
}
