package com.sbs.board;

import java.sql.*;
import java.time.LocalDateTime;

public class JDBCSelectTest {
  public static void main(String[] args) {
    // 1. 데이터베이스 연결 정보 설정 (식당 주소와 예약 정보라고 생각하세요)
    String url = "jdbc:mysql://127.0.0.1:3306/JDBC_board?useSSL=false&autoReconnect=true&serverTimezone=Asia/Seoul&characterEncoding=UTF-8&useUnicode=true&allowPublicKeyRetrieval=true";
    String username = "sbsst";
    String password = "sbs123414";

    // 2. 연결 객체와 PreparedStatement, ResultSet 객체 선언
    // (식당에 가기 위한 차, 주문서, 그리고 받아올 음식을 담을 그릇)
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

      // 5. 모든 게시글 조회하기
      System.out.println("\n=== 모든 게시글 조회 ===");
      selectAllArticles(conn);

      // 6. 특정 ID의 게시글 조회하기
      System.out.println("\n=== ID로 게시글 조회 ===");
      selectArticleById(conn, 1); // ID가 1인 게시글 조회

      // 7. 제목으로 게시글 검색하기
      System.out.println("\n=== 제목으로 게시글 검색 ===");
      searchArticlesByTitle(conn, "제목"); // "테스트"라는 단어가 제목에 포함된 게시글 검색

    } catch (ClassNotFoundException e) {
      // 드라이버를 찾을 수 없을 때 (면허증을 잃어버렸을 때)
      System.out.println("JDBC 드라이버를 찾을 수 없습니다: " + e.getMessage());
    } catch (SQLException e) {
      // SQL 관련 오류 발생 시 (주문이 거절되거나 식당에 입장할 수 없을 때)
      System.out.println("데이터베이스 오류 발생: " + e.getMessage());
    } finally {
      // 8. 사용한 자원 해제 (식당을 떠나기 전에 정리)
      try {
        // ResultSet 닫기 (음식 그릇 정리)
        if (rs != null && !rs.isClosed()) rs.close();
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

  // 모든 게시글을 조회하는 메서드
  private static void selectAllArticles(Connection conn) throws SQLException {
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      // SQL 쿼리 준비 (메뉴판에서 모든 음식 보기)
      String sql = "SELECT * FROM article ORDER BY id DESC";
      pstmt = conn.prepareStatement(sql);

      // 쿼리 실행하고 결과 받기 (주문한 모든 음식 받기)
      rs = pstmt.executeQuery();

      // 결과 처리 (받은 음식 확인하기)
      displayArticles(rs);

    } finally {
      // 자원 해제
      if (rs != null && !rs.isClosed()) rs.close();
      if (pstmt != null && !pstmt.isClosed()) pstmt.close();
    }
  }

  // ID로 특정 게시글을 조회하는 메서드
  private static void selectArticleById(Connection conn, int articleId) throws SQLException {
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      // SQL 쿼리 준비 (특정 번호의 메뉴만 보기)
      String sql = "SELECT * FROM article WHERE id = ?";
      pstmt = conn.prepareStatement(sql);

      // 파라미터 설정 (원하는 메뉴 번호 지정)
      pstmt.setInt(1, articleId);

      // 쿼리 실행하고 결과 받기 (특정 메뉴 가져오기)
      rs = pstmt.executeQuery();

      // 결과 처리 (받은 특정 메뉴 확인하기)
      displayArticles(rs);

    } finally {
      // 자원 해제
      if (rs != null && !rs.isClosed()) rs.close();
      if (pstmt != null && !pstmt.isClosed()) pstmt.close();
    }
  }

  // 제목으로 게시글을 검색하는 메서드
  private static void searchArticlesByTitle(Connection conn, String keyword) throws SQLException {
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      // SQL 쿼리 준비 (메뉴 이름에 특정 단어가 포함된 것만 찾기)
      String sql = "SELECT * FROM article WHERE title LIKE ? ORDER BY id DESC";
      pstmt = conn.prepareStatement(sql);

      // 파라미터 설정 (찾고 싶은 단어 지정)
      pstmt.setString(1, "%" + keyword + "%");  // %는 와일드카드로, 앞뒤 어디든 해당 단어가 포함된 것을 찾음

      // 쿼리 실행하고 결과 받기 (조건에 맞는 메뉴 가져오기)
      rs = pstmt.executeQuery();

      // 결과 처리 (찾은 메뉴들 확인하기)
      displayArticles(rs);

    } finally {
      // 자원 해제
      if (rs != null && !rs.isClosed()) rs.close();
      if (pstmt != null && !pstmt.isClosed()) pstmt.close();
    }
  }

  // ResultSet에서 게시글 정보를 출력하는 메서드
  private static void displayArticles(ResultSet rs) throws SQLException {
    boolean found = false;

    while (rs.next()) {
      found = true;
      // 각 컬럼의 데이터 가져오기
      int id = rs.getInt("id");
      LocalDateTime regDate = rs.getTimestamp("regDate").toLocalDateTime();
      LocalDateTime updateDate = rs.getTimestamp("updateDate").toLocalDateTime();
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
