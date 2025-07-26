package com.sbs.board.global.simpleDb;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Sql {
  private final SimpleDb simpleDb;
  private final StringBuilder sqlBuilder;
  private final List<Object> params;

  public Sql(SimpleDb simpleDb) {
    this.simpleDb = simpleDb;
    this.sqlBuilder = new StringBuilder();
    this.params = new ArrayList<>();
  }

  public Sql append(String sqlPart, Object... args) {
    sqlBuilder.append(sqlPart).append(" ");
    Collections.addAll(params, args);
    return this;
  }

  public Sql appendIn(String sqlPart, Object... args) {
    String replacement = IntStream
        .range(0, args.length)
        .boxed()
        .map(i -> "?")
        .collect(Collectors.joining(", "));

    sqlPart = sqlPart.replace("?", replacement);
    sqlBuilder.append(sqlPart).append(" ");
    Collections.addAll(params, args);

    return this;
  }

  public long insert() {
    String sql = sqlBuilder.toString();
    return simpleDb.runInsert(sql, params.toArray());
  }

  public long update() {
    String sql = sqlBuilder.toString();
    return simpleDb.runUpdate(sql, params.toArray());
  }

  public long delete() {
    String sql = sqlBuilder.toString();
    return simpleDb.runUpdate(sql, params.toArray());
  }

  public <T> List<T> selectRows() {
    return selectRows((Class<T>) Map.class);
  }

  public <T> List<T> selectRows(Class<T> cls) {
    String sql = sqlBuilder.toString();
    return simpleDb.runSelectRows(sql, params.toArray(), cls);
  }

  public <T> T selectRow(Class<T> cls) {
    String sql = sqlBuilder.toString();
    return simpleDb.runSelectRow(sql, params.toArray(), (Class<T>) cls);
  }

  public Map<String, Object> selectRow() {
    String sql = sqlBuilder.toString();
    return simpleDb.runSelectRow(sql, params.toArray());
  }

  public LocalDateTime selectDatetime() {
    String sql = sqlBuilder.toString();
    return simpleDb.runSelectDatetime(sql, params.toArray());
  }

  public Long selectLong() {
    String sql = sqlBuilder.toString();
    return simpleDb.runSelectLong(sql, params.toArray());
  }

  public String selectString() {
    String sql = sqlBuilder.toString();
    return simpleDb.runSelectString(sql, params.toArray());
  }

  public Boolean selectBoolean() {
    String sql = sqlBuilder.toString();
    return simpleDb.runSelectBoolean(sql, params.toArray());
  }

  public List<Long> selectLongs() {
    String sql = sqlBuilder.toString();
    return simpleDb.runSelectLongs(sql, params.toArray());
  }

  public List<String> selectStrings() {
    String sql = sqlBuilder.toString();
    return simpleDb.runSelectStrings(sql, params.toArray());
  }

  public List<LocalDateTime> selectDatetimes() {
    String sql = sqlBuilder.toString();
    return simpleDb.runSelectDatetimes(sql, params.toArray());
  }

  public List<Boolean> selectBooleans() {
    String sql = sqlBuilder.toString();
    return simpleDb.runSelectBooleans(sql, params.toArray());
  }
}
