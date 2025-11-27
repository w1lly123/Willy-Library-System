Willy線上圖書借閱系統-快速啟動指南

本專案使用Spring Boot + Vue.js + PostgreSQL開發。請依照以下三步驟啟動系統。

1.資料庫準備(PostgreSQL)

開啟pgAdmin或終端機，建立一個新的資料庫，命名為library_db。

依照順序執行專案根目錄 DB/ 資料夾下的三個 SQL 檔案：

library_ddl.sql (建立資料表)

borrow_book.sql (建立借書功能程序)

library_dml.sql (匯入測試書籍資料)

2.後端啟動(Spring Boot)

開啟設定檔 src/main/resources/application.properties。

修改資料庫密碼與Port(避免衝突建議用8081)：

spring.datasource.password=你的PostgreSQL密碼
server.port=8081


在專案根目錄開啟終端機(Terminal)，執行啟動指令：

mvn spring-boot:run


看到 Started LibrarySystemApplication 字樣代表啟動成功。

3.前端啟動(Vue.js)

進入 vue-frontend/ 資料夾。

直接使用瀏覽器(Chrome/Edge)開啟 index.html 檔案。

重要檢查：若無法連線，請用記事本打開 index.html，確認第 116 行的設定與後端 Port 一致：

const API_BASE = 'http://localhost:8081/api';


測試帳號：您可以在網頁上直接註冊新帳號進行測試。