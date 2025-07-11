<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<style>
	body {
	    margin: 0;
	    font-family: Arial, sans-serif;
	    background-color: #f4f4f4;
	    display: flex;
	    justify-content: center;
	    align-items: center;
	    min-height: 100vh;
	    overflow: hidden;
	}
	
	.maintenance-overlay {
	    position: fixed;
	    top: 0;
	    left: 0;
	    width: 100%;
	    height: 100%;
	    background-color: rgba(0, 0, 0, 0.8);
	    display: flex;
	    justify-content: center;
	    align-items: center;
	    z-index: 1000;
	    color: #fff;
	    text-align: center;
	    padding: 20px;
	    box-sizing: border-box;
	}
	
	.maintenance-content {
	    background-color: #2c3e50;
	    padding: 40px;
	    border-radius: 10px;
	    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.5);
	    max-width: 600px;
	    width: 90%;
	}
	
	.maintenance-title {
	    font-size: 2.0em;
	    margin-bottom: 20px;
	    color: #f39c12;
	}
	
	.maintenance-message {
	    font-size: 1.2em;
	    line-height: 1.6;
	    margin-bottom: 30px;
	}
	
	.home-button {
	    display: inline-block;
	    background-color: #3498db;
	    color: white;
	    padding: 12px 25px;
	    border-radius: 5px;
	    text-decoration: none;
	    font-size: 1em;
	    transition: background-color 0.3s ease;
	}
	
	.home-button:hover {
	    background-color: #2980b9;
	}
		
</style>
</head>
<body>
	<%@ include file="/WEB-INF/views/include/loginInfo.jsp" %>
	
	<div class="maintenance-overlay">
        <div class="maintenance-content">
            <h1 class="maintenance-title">🚧 현재 페이지는 개발 중입니다 🚧</h1>
            <p class="maintenance-message">
                더 나은 서비스를 위해 페이지를 개선하고 있습니다.<br>
                빠른 시일 내에 찾아뵙겠습니다. 감사합니다.
            </p>
            <a href="/" class="home-button">홈으로 돌아가기</a>
        </div>
    </div>
	
	<%@ include file="/WEB-INF/views/include/footer.jsp" %>
</body>
</html>