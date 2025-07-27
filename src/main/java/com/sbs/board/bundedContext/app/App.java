package com.sbs.board.bundedContext.app;

import com.sbs.board.bundedContext.article.controller.ArticleController;
import com.sbs.board.bundedContext.article.dto.Article;
import com.sbs.board.bundedContext.container.Container;
import com.sbs.board.global.base.Rq;
import com.sbs.board.global.simpleDb.SimpleDb;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class App {
  private Scanner sc;
  private SimpleDb simpleDb;
  private ArticleController articleController;

  public App() {
    sc = Container.sc;

    simpleDb = new SimpleDb("localhost", "sbsst", "sbs123414", "JDBC_board");
    simpleDb.setDevMode(true); // 개발 모드 활성화 (디버깅을 위해 SQL 쿼리 출력)

    articleController = Container.articleController;
  }

  public void run() {
    System.out.println("== 게시판 프로그램 시작 ==");

    while (true) {
      Rq rq = new Rq();

      System.out.print("명령어) ");
      String cmd = sc.nextLine().trim();

      rq.setCommand(cmd, simpleDb);

      if (cmd.equals("/usr/article/write")) {
        articleController.doWrite(rq);
      } else if (cmd.equals("/usr/article/list")) {
        System.out.println("== 게시글 목록 ==");

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

          // 쿼리 준비
          String sql = "SELECT * FROM article ORDER BY id DESC";
          pstmt = conn.prepareStatement(sql);

          // 쿼리 실행
          rs = pstmt.executeQuery();

          // 5. 모든 게시글 조회하기
          System.out.println("번호 | 제목 | 작성일");
          while (rs.next()) {
            // 각 컬럼의 데이터 가져오기
            int id = rs.getInt("id");
            LocalDateTime regDate = rs.getTimestamp("regDate").toLocalDateTime();
            LocalDateTime updateDate = rs.getTimestamp("updateDate").toLocalDateTime();
            String title = rs.getString("title");
            String content = rs.getString("content");

            System.out.printf("%d | %s | %s\n", id, title, regDate);
          }

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

      } else if (cmd.equals("exit")) {
        System.out.println("프로그램을 종료합니다.");
        break;
      } else {
        System.out.println("알 수 없는 명령어입니다.");
      }
    }

    System.out.println("== 게시판 프로그램 끝 =");
    sc.close();
  }
}
