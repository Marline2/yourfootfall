# 당신의발걸음(your footfalls): 센서를 사용한 이동 거리
안드로이드 스튜디오를 사용한 이동 거리 측정 애플리케이션 <br/>
구글 플레이 스토어에서 만나보실 수 있습니다!<br/>
https://play.google.com/store/apps/details?id=com.mar.yourfootfall<br/><br/>



시연 영상 : https://youtube.com/shorts/wp0_bpzRzao?feature=share <br/>
기기에 내장된 가속도 센서, 지자기 센서, 기압 센서를 이용하여 만든 앱입니다. <br/>
총 이동거리와 사용자의 위치에서 바라본 방향으로 이동한 거리, 총 이동 고도와 이동한 높이를 나타냅니다. <br/>
(**기기를 손에 쥐고 흔들며 걸어야 이동 거리의 정확한 측정이 이뤄집니다.) <br/><br/>

즉, 사용자 위치를 기준으로 한 X, Y, Z축의 값을 보여줍니다. <br/>

### 애플리케이션 개발 환경
- Android Studio @2021.01.01 Patch 3

### 애플리케이션 버전
- minSdkVersion 16
- targetSdkVersion 32

### 애플리케이션 주요 코드
- 가속도 센서를 활용한 총 거리 값 표현
- 가속도 센서와 지자기 센서를 활용한 사용자 중심의 방향 값(앞뒤좌우) 및 각 방향의 이동 거리 표현
- 기압 센서를 활용한 총 이동 높이(고도), 사용자 중심의 방향 값(상하) 및 각 방향의 이동 높이 표현

#### 구성 페이지
메인 화면을 구성하는 MainActivity.java와 activity_main.xml이 있습니다. <br/>

#### 발전할 점
1. 코드의 변수명이 대체적으로 깔끔하지 못한 것이 아쉽습니다.


