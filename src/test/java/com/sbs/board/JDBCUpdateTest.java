package com.sbs.board;

import java.sql.*;

public class JDBCUpdateTest {
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

      // 5. 업데이트 전 게시글 조회하기
      System.out.println("\n=== 업데이트 전 게시글 ===");
      selectArticleById(conn, 1); // ID가 1인 게시글 조회

      // 6. 게시글 업데이트하기
      System.out.println("\n=== 게시글 업데이트 수행 ===");
      int articleId = 1; // 업데이트할 게시글 ID
      String newTitle = "수정된 제목입니다";
      String newContent = "이 내용은 JDBC UPDATE 테스트로 수정되었습니다.";

      updateArticle(conn, articleId, newTitle, newContent);

      // 7. 업데이트 후 게시글 조회하기
      System.out.println("\n=== 업데이트 후 게시글 ===");
      selectArticleById(conn, articleId);

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

  // 특정 ID의 게시글을 업데이트하는 메서드
  private static void updateArticle(Connection conn, int articleId, String newTitle, String newContent) throws SQLException {
    PreparedStatement pstmt = null;

    try {
      // SQL 쿼리 준비 (주문서의 내용을 수정하는 것과 같습니다)
      String sql = "UPDATE article";
      sql += " SET updateDate = NOW()";
      sql += ", title = '%s'".formatted(newTitle);
      sql += ", content = '%s'".formatted(newContent);
      sql += " WHERE id = %d".formatted(articleId);

      pstmt = conn.prepareStatement(sql);

      // 쿼리 실행 (수정 요청 전달)
      int affectedRows = pstmt.executeUpdate();

      // 결과 확인
      if (affectedRows > 0) {
        System.out.println("게시글 ID " + articleId + "가 성공적으로 업데이트되었습니다.");
      } else {
        System.out.println("게시글 ID " + articleId + "를 찾을 수 없거나 업데이트에 실패했습니다.");
      }

    } finally {
      // 자원 해제
      if (pstmt != null) pstmt.close();
    }
  }

  // ID로 특정 게시글을 조회하는 메서드
  private static void selectArticleById(Connection conn, int articleId) throws SQLException {
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      // SQL 쿼리 준비
      String sql = "SELECT * FROM article WHERE id = ?";
      pstmt = conn.prepareStatement(sql);

      // 파라미터 설정
      pstmt.setInt(1, articleId);

      // 쿼리 실행하고 결과 받기
      rs = pstmt.executeQuery();

      // 결과 처리
      displayArticles(rs);

    } finally {
      // 자원 해제
      if (rs != null) rs.close();
      if (pstmt != null) pstmt.close();
    }
  }

  // ResultSet에서 게시글 정보를 출력하는 메서드
  private static void displayArticles(ResultSet rs) throws SQLException {
    boolean found = false;

    while (rs.next()) {
      found = true;
      // 각 컬럼의 데이터 가져오기
      int id = rs.getInt("id");
      String regDate = rs.getString("regDate");
      String updateDate = rs.getString("updateDate");
      String title = rs.getString("title");
      String content = rs.getString("content");

      // 게시글 정보 출력
      System.out.println("게시글 ID: " + id);
      System.out.println("등록일: " + regDate);
      System.out.println("수정일: " + updateDate);
      System.out.println("제목: " + title);
      System.out.println("내용: " + content);
      System.out.println("-----------------------------");
    }

    if (!found) {
      System.out.println("조회된 게시글이 없습니다.");
    }
  }
}
