package step5;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.reactivex.Observable;

public class ObservableCreationTest {

	public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

	public static String getLogString(String msg){
		return Thread.currentThread().getName() + " | " + LocalDateTime.now().format(formatter) + " | " + msg;
	}

	@Test
	@DisplayName("Observable_interval()_메서드연습")
	public void Observable_interval_메서드연습() throws Exception{
		Observable.interval(0L, 1000L, TimeUnit.MILLISECONDS)
			.map(second -> second + " 초 ...")
			.subscribe(secondString -> System.out.println(getLogString(secondString)));

		// Thread.sleep(3000L);
		Thread.sleep(5000L);
	}

	@Test
	@DisplayName("Observable_range()_메서드연습")
	public void Observable_range_메서드연습(){
		System.out.println("Example #1");
		Observable.range(1, 10)
			.subscribe(n -> System.out.println(getLogString(String.valueOf(n))));

		System.out.println();
		System.out.println("Example #2");
		Observable.range(0, 10)
			.subscribe(n -> System.out.println(getLogString(String.valueOf(n))));
	}

	@Test
	@DisplayName("Observable_timer()_메서드연습")
	public void Observable_timer_메서드연습() throws Exception{
		System.out.println(getLogString("스타트..."));
		Observable<String> observable = Observable.timer(2000, TimeUnit.MILLISECONDS)
			.map(count -> count + " 초가 지났습니다~");

		observable.subscribe(data -> System.out.println(getLogString(data)));

		Thread.sleep(3000L);
	}

	/**
	 * subscribe()가 호출될 때마다(구독이 발생할 때마다) 새로운 Observable 을 생성한다.
	 * 선언한 시점의 데이터를 통지하는 것이 아니라 호출시점의 데이터를 통지한다.
	 * 메서드 생성을 미루는 효과가 있기 때문에 최신 데이터를 얻고자 할 때 사용한다.
	 *
	 * 참고)
	 * 마블다이어그램 & API
	 *  http://reactivex.io/RxJava/javadoc/io/reactivex/Flowable.html#defer-java.util.concurrent.Callable-
	 *  Returns a Flowable that calls a Publisher factory to create a Publisher for each new Subscriber that subscribes.
	 *  That is, for each subscriber, the actual Publisher that subscriber observes is determined by the factory function.
	 *
	 * 참고) 블로그 설명
	 * https://jeongupark-study-house.tistory.com/94
	 */
	@Test
	@DisplayName("Observable_defer()_메서드연습")
	public void Observable_defer_메서드연습() throws Exception{
		// defer() 는 매번 새로운 Observable 을 생성해낸다.
		Observable<LocalDateTime> deferObserver = Observable.defer(()->{
			LocalDateTime currentTime = LocalDateTime.now();
			return Observable.just(currentTime);
		});

		// just() 는 생성 시점의 데이터를 가지고 통지(전달)한다.
		Observable<LocalDateTime> justObserver = Observable.just(LocalDateTime.now());

		deferObserver.subscribe(time -> System.out.println(getLogString("첫번째 Observable.defer() 로 구독한 시각 " + time)));
		justObserver.subscribe(time -> System.out.println(getLogString("첫번째 Observable.just() 로 구독한 시각 " + time)));

		Thread.sleep(3000L);

		deferObserver.subscribe(time -> System.out.println(getLogString("두번째 Observable.defer() 로 구독한 시각 " + time)));
		justObserver.subscribe(time -> System.out.println(getLogString("두번째 Observable.just() 로 구독한 시각 " + time)));

		// 실제로 확인해보면,
		// 	Observable.defer()로 구독하는 데이터는 매번 새로운 시간 데이터이다.
		//  Observable.just()로 구독하는 데이터는 생성시 초기에 전달해준 데이터 그대로이다.
	}

	/**
	 * Iterable 을 구현하는 컬렉션 타입 객체들을 받아서 생성된다.
	 * 마블다이어그램 참고
	 *  http://reactivex.io/RxJava/javadoc/io/reactivex/Flowable.html#fromIterable-java.lang.Iterable-
	 */
	@Test
	@DisplayName("Observable_fromIterable()_메서드연습")
	public void Observable_fromIterable_메서드연습(){
		List<String> employees = Arrays.asList("박지성", "손흥민", "황의조", "지드래곤", "태양");

		Observable.fromIterable(employees)
			.subscribe(player -> System.out.println(getLogString(player)));
	}

	/**
	 * Future 는 Java5 에서부터 도입된 비동기 처리를 위한 API 이다.
	 * 처리시간이 오래 걸리는 작업은 Future 에 맡겨두고 그 사이에 다른 작업을 할 수 있기에 결과적으로 처리 시간이 짧아진다.
	 * 마블다이어그램
	 *   http://reactivex.io/RxJava/javadoc/io/reactivex/Observable.html#fromFuture-java.util.concurrent.Future-
	 * 내부적으로 get() 함수를 호출해서 처리 시간이 짧은 작업이 끝나더라도 처리시간이 오래걸리는 작업이 끝날 때까지 기다려준다.
	 *
	 * Future 인터페이스는 자바5에서 비동기 처리를 위해 추가된 동시성 API이다.
	 * 시간이 오래 걸리는 작업은 Future 를 반환하는 ExecutorService 에게 맡기고 비동기로 다른 작업을 수행할 수 있다.
	 * Java8 에서는 CompletableFuture 클래스를 통해 구현이 간결해졌다.
	 */
	@Test
	@DisplayName("Observable_fromFuture()_메서드연습__future를_사용하지_않는_동기식호출방식")
	public void Observable_fromFuture_Future를_사용하지_않는_동기식호출방식(){
		long startTime = System.currentTimeMillis();

		// GPS 좌표 조회 외부 API 호출
		Gps gps = syncFindAddressGps("성남 복정 2");
		System.out.println(getLogString(" # GPS 조회를 완료했습니다."));
		System.out.println(getLogString(" # GPS 좌표는 " + gps + " 입니다."));

		// 이메일 정합성 체크
		checkEmailExist("abc@gmail.com");
		// 비밀번호 정합성 체크
		checkPasswordValidate("12345");

		long endTime = System.currentTimeMillis();

		System.out.println();
		System.out.println(getLogString("처리시간 :: " + String.valueOf((endTime - startTime)/1000.0) + " 초"));
	}

	@Test
	@DisplayName("Observable_fromFuture()_메서드연습__Future를_사용한_비동기식호출방식")
	public void Observable_fromFuture_Future를_사용한_비동기식호출방식(){
		long startTime = System.currentTimeMillis();

		Future<Gps> future = asyncFindAddressGps("성남 복정 2");

		// 이메일 정합성 체크
		checkEmailExist("abc@gmail.com");
		// 비밀번호 정합성 체크
		checkPasswordValidate("12345");

		try {
			future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();
		System.out.println();
		System.out.println(getLogString("처리시간 :: " + String.valueOf((endTime - startTime)/1000.0) + " 초"));
	}

	@Test
	@DisplayName("Observable_fromFuture()_메서드연습_fromFuture를_이용한_리액티브방식")
	public void Observable_fromFuture_메서드연습_fromFuture를_이용한_리액티브방식(){
		long startTime = System.currentTimeMillis();

		Future<Gps> future = asyncFindAddressGps("성남 복정 2");

		// 이메일 정합성 체크
		checkEmailExist("abc@gmail.com");
		// 비밀번호 정합성 체크
		checkPasswordValidate("12345");

		Observable.fromFuture(future)
			.subscribe(gpsData -> System.out.println(getLogString(String.valueOf(gpsData))));

		long endTime = System.currentTimeMillis();
		System.out.println();
		System.out.println(getLogString("처리시간 :: " + String.valueOf((endTime - startTime)/1000.0) + " 초"));
	}

	public Gps syncFindAddressGps(String address){
		System.out.println(getLogString(address + "의 GPS 좌표 얻어오는 중 ... (예상 : 5s)"));
		delay(5);
		return new Gps(100,200);
	}

	public Future<Gps> asyncFindAddressGps(String address){
		CompletableFuture<Gps> future = CompletableFuture.supplyAsync(() -> syncFindAddressGps(address));
		return future;
	}

	static class Gps{
		int x;
		int y;

		Gps(int x, int y){
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		@Override
		public String toString() {
			return "Gps{" +
				"x=" + x +
				", y=" + y +
				'}';
		}
	}

	public void checkEmailExist(String email){
		delay(2);
		System.out.println(getLogString(" 이메일 체크 완료 (예상 : 2s)"));
	}

	public void checkPasswordValidate(String password){
		delay(1);
		System.out.println(getLogString(" 패스워드 정합성 체크 완료 (예상 : 1s)"));
	}

	public void delay(int second){
		try{
			Thread.sleep(second * 1000L);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
