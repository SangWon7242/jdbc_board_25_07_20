package com.sbs.board;

import java.sql.*;

public class JDBCInsertTest {
  public static void main(String[] args) {
    // 1. 데이터베이스 연결 정보 설정 (식당 주소와 예약 정보라고 생각하세요)
    String url = "jdbc:mysql://127.0.0.1:3306/JDBC_board?useSSL=false&autoReconnect=true&serverTimezone=Asia/Seoul&characterEncoding=UTF-8&useUnicode=true&allowPublicKeyRetrieval=true";
    String username = "sbsst";
    String password = "sbs123414";

    // 2. 연결 객체와 PreparedStatement 객체 선언 (식당에 가기 위한 차와 주문서라고 생각하세요)
    Connection conn = null;
    PreparedStatement pstmt = null;

    try {
      // 3. JDBC 드라이버 로드 (차를 운전하기 위한 면허증 준비)
      Class.forName("com.mysql.cj.jdbc.Driver");
      System.out.println("드라이버 로드 성공!");

      // 4. 데이터베이스에 연결 (식당에 도착)
      conn = DriverManager.getConnection(url, username, password);
      System.out.println("데이터베이스 연결 성공!");

      // 6. SQL 쿼리 준비 (주문서 작성)
      // PreparedStatement를 사용하면 SQL 인젝션 공격을 방지할 수 있습니다
      String sql = "INSERT INTO article";
      sql += " SET regDate = NOW()";
      sql += ", updateDate = NOW()";
      sql += ", title = CONCAT(\"제목\", RAND())";
      sql += ", content = CONCAT(\"내용\", RAND());";

      /*
      INSERT INTO article
      SET regDate = NOW(),
      updateDate = NOW(),
      title = '제목1',
      content = '내용1';
      */

      // prepareStatement : 완성된 SQL 문장을 준비하는 메서드
      // Statement.RETURN_GENERATED_KEYS : 자동 생성된 키 값을 반환받기 위한 옵션
      pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

      // 8. 쿼리 실행 (주문서 제출)
      int affectedRows = pstmt.executeUpdate();
      System.out.println(affectedRows + "개의 행이 추가되었습니다.");

      // 9. 자동 생성된 ID 값 가져오기 (영수증 확인)
      if (affectedRows > 0) {
        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        if (generatedKeys.next()) {
          long id = generatedKeys.getLong(1);
          System.out.println("생성된 게시글 ID: " + id);
        }
      }

    } catch (ClassNotFoundException e) {
      // 드라이버를 찾을 수 없을 때 (면허증을 잃어버렸을 때)
      System.out.println("JDBC 드라이버를 찾을 수 없습니다: " + e.getMessage());
    } catch (SQLException e) {
      // SQL 관련 오류 발생 시 (주문이 거절되거나 식당에 입장할 수 없을 때)
      System.out.println("데이터베이스 오류 발생: " + e.getMessage());
    } finally {
      // 10. 사용한 자원 해제 (식당을 떠나기 전에 정리)
      try {
        // PreparedStatement 닫기 (주문서 반납)
        if (pstmt != null && !pstmt.isClosed()) pstmt.close();
        // Connection 닫기 (식당 나가기)
        if (conn != null && !conn.isClosed()) {
          conn.close();
          System.out.println("데이터베이스 연결 종료!");
        }
      } catch (SQLException e) {
        System.out.println("자원 해제 중 오류 발생: " + e.getMessage());
      }
    }
  }
}
