Willy線上圖書借閱系統

本專案使用Spring Boot + Vue.js + PostgreSQL進行開發。請依照以下三步驟啟動系統。

1. 資料庫準備 (PostgreSQL)

請開啟pgAdmin或terminal，建立一個新的資料庫，命名為library_db。

接著，請依照順序執行專案根目錄 DB/ 資料夾下的三個SQL檔案：

library_ddl.sql(建立資料表)

project_procedures.sql(建立預存程序)

library_dml.sql(匯入測試資料)

2. 後端啟動(Spring Boot)

開啟設定檔：
library-sys/library-spring-backend/src/main/resources/application.properties

修改資料庫密碼與Port：

spring.datasource.password=你的PostgreSQL密碼
server.port=8081


開啟終端機，進入後端目錄：

cd library-backend


執行啟動指令：

mvn spring-boot:run


當看到 Started LibrarySystemApplication 時，代表後端啟動成功。

3. 前端啟動(Vue + Vite)

開啟新的終端機，並進入前端目錄：

cd library-frontend


啟動開發伺服器(預設Port:5173)：

npm run dev


開啟瀏覽器訪問：http://localhost:5173

測試帳號

系統啟動後，請直接在網頁上點擊「註冊」按鈕，建立新帳號進行測試。
