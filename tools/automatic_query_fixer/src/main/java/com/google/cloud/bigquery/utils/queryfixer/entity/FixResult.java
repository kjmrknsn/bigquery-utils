package com.google.cloud.bigquery.utils.queryfixer.entity;

import com.google.cloud.bigquery.utils.queryfixer.errors.BigQuerySqlError;
import lombok.Builder;
import lombok.Value;

import java.util.List;

/** A value class represents the result of fixing an error in a query. */
@Builder
@Value
public class FixResult {

  /** Status of fixing the error. It is either ERROR_FIXED, NO_ERROR. or Failure. */
  Status status;
  /** A list of options to fix this error. */
  List<FixOption> options;
  /** The error message of the error to be fixed */
  String error;

  /**
   * An overview on how the query fixer will fix this error. The details of the fixing will be
   * presented in the options field.
   */
  String approach;

  /** The position at the query where the error occurs. */
  Position errorPosition;

  /**
   * Create a Failure FixResult indicating that a {@link BigQuerySqlError} can not be fixed.
   *
   * @param error un-fixable error
   * @return FixResult with FAILURE Status
   */
  public static FixResult failure(BigQuerySqlError error) {
    return FixResult.builder()
        .status(Status.FAILURE)
        .error(error.getErrorSource().getMessage())
        .errorPosition(error.getErrorPosition())
        .build();
  }

  /**
   * Create a Success FixResult with the details on fixing a {@link BigQuerySqlError} in a query.
   *
   * @param approach approach to fix the error
   * @param options detailed options to fix the error by the given approach.
   * @param error error to fix
   * @return FixResult with ERROR_FIXED Status
   */
  public static FixResult success(
      String approach, List<FixOption> options, BigQuerySqlError error) {
    return FixResult.builder()
        .status(Status.ERROR_FIXED)
        .options(options)
        .approach(approach)
        .error(error.getErrorSource().getMessage())
        .errorPosition(error.getErrorPosition())
        .build();
  }

  /**
   * Create a FixResult indicating a query has no error.
   *
   * @return FixResult with NO_ERROR Status
   */
  public static FixResult noError() {
    return FixResult.builder().status(Status.NO_ERROR).build();
  }

  public enum Status {
    NO_ERROR,
    ERROR_FIXED,
    FAILURE
  }
}
