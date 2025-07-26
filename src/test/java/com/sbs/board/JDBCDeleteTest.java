package com.sbs.board;

import java.sql.*;

public class JDBCDeleteTest {
  public static void main(String[] args) {
    // 1. 데이터베이스 연결 정보 설정 (식당 주소와 예약 정보라고 생각하세요)
    String url = "jdbc:mysql://127.0.0.1:3306/JDBC_board?useSSL=false&autoReconnect=true&serverTimezone=Asia/Seoul&characterEncoding=UTF-8&useUnicode=true&allowPublicKeyRetrieval=true";
    String username = "sbsst";
    String password = "sbs123414";

    // 2. 연결 객체와 PreparedStatement 객체 선언
    // (식당에 가기 위한 차와 주문 수정서라고 생각하세요)
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      // 3. JDBC 드라이버 로드 (차를 운전하기 위한 면허증 준비)
      Class.forName("com.mysql.cj.jdbc.Driver");
      System.out.println("드라이버 로드 성공!");

      // 4. 데이터베이스에 연결 (식당에 도착)
      conn = DriverManager.getConnection(url, username, password);
      System.out.println("데이터베이스 연결 성공!");

      // 특정 ID의 게시글 삭제하기
      System.out.println("\n=== 특정 ID의 게시글 삭제 ===");
      int articleIdToDelete = 2; // 삭제할 게시글 ID
      deleteArticleById(conn, articleIdToDelete);

    } catch (ClassNotFoundException e) {
      // 드라이버를 찾을 수 없을 때 (면허증을 잃어버렸을 때)
      System.out.println("JDBC 드라이버를 찾을 수 없습니다: " + e.getMessage());
    } catch (SQLException e) {
      // SQL 관련 오류 발생 시 (주문 수정이 거절되거나 식당에 입장할 수 없을 때)
      System.out.println("데이터베이스 오류 발생: " + e.getMessage());
    } finally {
      // 10. 사용한 자원 해제 (식당을 떠나기 전에 정리)
      try {
        // ResultSet 닫기
        if (rs != null) {
          rs.close();
        }
        // PreparedStatement 닫기 (주문 수정서 반납)
        if (pstmt != null) {
          pstmt.close();
        }
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

  // 특정 ID의 게시글을 삭제하는 메서드
  private static void deleteArticleById(Connection conn, int articleId) throws SQLException {
    PreparedStatement pstmt = null;

    try {
      // SQL 쿼리 준비 (특정 주문을 취소하는 것과 같습니다)
      String sql = "DELETE FROM article WHERE id = ?";
      pstmt = conn.prepareStatement(sql);

      // 파라미터 설정 (취소할 주문 번호 지정)
      pstmt.setInt(1, articleId);

      // 쿼리 실행 (취소 요청 전달)
      int affectedRows = pstmt.executeUpdate();

      // 결과 확인
      if (affectedRows > 0) {
        System.out.println("게시글 ID " + articleId + "가 성공적으로 삭제되었습니다.");
      } else {
        System.out.println("게시글 ID " + articleId + "를 찾을 수 없거나 삭제에 실패했습니다.");
      }

    } finally {
      // 자원 해제
      if (pstmt != null) pstmt.close();
    }
  }
}
