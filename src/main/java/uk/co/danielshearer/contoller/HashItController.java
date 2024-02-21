package uk.co.danielshearer.contoller;

import java.util.Arrays;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HashItController {
  private final Logger logger = LoggerFactory.getLogger(HashItController.class);

  // #Oops. Bad Random
  private final Random random = new Random(0);
  private final JdbcTemplate jdbcTemplate;

  public HashItController(final JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @GetMapping(path = "/hashit", produces = "text/html")
  public String hashit(@RequestParam("value") final String value) throws Exception {
    final int id = (int) random.nextLong();

    // #Oops. Shell Injection
    final String[] cmdarray = {"sh", "-c", "echo " + value + " | md5sum"};
    logger.info("Executing command: " + Arrays.toString(cmdarray));
    final Process proc = Runtime.getRuntime().exec(cmdarray);
    proc.waitFor();

    final byte[] stdErr = proc.getErrorStream().readAllBytes();
    final byte[] stdOut = proc.getInputStream().readAllBytes();

    // #Oops. Default Charset
    final String outputString = new String(stdOut);
    final String errorString = new String(stdErr);

    final String sql = String.format("insert into hash_table (id, input, hash) values ('%s', '%s', '%s')", id, value,
      outputString);
    logger.info("Executing update: " + sql);
    jdbcTemplate.execute(sql);

    return "id: " + id + "\nStdOut: " + outputString + "\nStdErr: " + errorString;
  }

  @GetMapping(path = "/gethash", produces = "text/html")
  public String gethash(@RequestParam("id") final String id) {
    final String sql = String.format("select * from hash_table where id='%s'", id);
    logger.info("Executing query: " + sql);
    final StringBuilder response = new StringBuilder();
    response.append("<html><head><title>Hash</title></head>");
    response.append("<body><ul>");
    jdbcTemplate.query(sql, rs -> {
      response.append("<li>").append(rs.getString("id")).append(". Input: ").append(rs.getString("input")).append(" " +
        "-> ").append(rs.getString("hash")).append("</li>");
    });
    response.append("</ul></body></html>");
    return response.toString();
  }
}
