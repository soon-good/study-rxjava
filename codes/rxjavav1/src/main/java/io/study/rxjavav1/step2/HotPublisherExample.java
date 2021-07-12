package io.study.rxjavav1.step2;

import io.reactivex.processors.PublishProcessor;

public class HotPublisherExample {
	public static void main(String [] args){
		PublishProcessor<String> processor = PublishProcessor.create();
		processor.subscribe(msg -> System.out.println("[구독자 1] " + msg));
		processor.onNext("방금 ");		// publisher 가 메시지를 발행함
		processor.onNext("밥 먹었잖아 ");	// publisher 가 메시지를 발행함

		processor.subscribe(msg -> System.out.println("[구독자 2] " + msg));
		processor.onNext("또 먹어? ");	// publisher 가 메시지를 발행함
		processor.onNext("후식이라고? ");	// publisher 가 메시지를 발행함
		processor.onNext("헐... ");		// publisher 가 메시지를 발행함

		processor.onComplete();
	}
}
