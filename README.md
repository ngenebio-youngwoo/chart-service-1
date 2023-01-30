변이검출플랫폼의 차트 생성 처리를 담당하는 서비스이다.

# 기본정보
기준버전: 0.1.0

작성일: 2023-01-27

### 구현 차트
HLA
- Base Variation Plot
- Coverage Plot

# 기술스택
chart 서비스는 아래의 주요 프레임워크에 대한 의존성이 있다.
- selenium
- selenium chrome driver
- [d3.js](https://d3js.org/)
- html, js, css

# result-service
차트 생성에 필요한 데이터는 [result-service](https://github.com/ngenebio-sw/result-service) 에 요청하도록 되어있다.

# Selenium 
chart-service에서는 html, d3.js 을 이용하여 차트를 렌더링 합니다. 이 차트 렌더링 html을 실행하려면 selenium을 통해 실행하도록 되어있다.

selenium의 chrome driver를 기준으로 구현되어 있으며 타 브라우저의 경우 다소 결과가 다를 수 있습니다. selenium의 chrome driver로 환경을 구성한다.

## chrome driver 버전
chrome driver 109.0 이상을 요구한다.