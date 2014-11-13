package cn.dreampie.mail;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ice on 14-11-13.
 */
public class ExecutorMailer {

  private static Logger logger = LoggerFactory.getLogger(Mailer.class);

  private static ExecutorService executorService = null;

  public static ExecutorService getExecutorService() {
    if (executorService == null)
      executorService = Executors.newCachedThreadPool();
    return executorService;
  }

  public static void setExecutorService(ExecutorService executorService) {
    ExecutorMailer.executorService = executorService;
  }

  /**
   * @param subject    主题
   * @param body       内容
   * @param recipients 收件人
   */
  public static void sendText(final String subject, final String body, final String... recipients) {
    getExecutorService().execute(getSendTextRunnable(subject, body, recipients));
  }


  private static Runnable getSendTextRunnable(final String subject, final String body, final String... recipients) {
    return new Runnable() {
      @Override
      public void run() {
        try {
          Mailer.sendText(subject, body, recipients);
        } catch (EmailException e) {
          e.printStackTrace();
        }
      }
    };
  }

  /**
   * @param subject    主题
   * @param body       内容
   * @param recipients 收件人
   */
  public static void sendHtml(final String subject, final String body, final String... recipients) throws EmailException {
    sendHtml(subject, body, null, recipients);
  }

  /**
   * @param subject    主题
   * @param body       内容
   * @param attachment 附件
   * @param recipients 收件人
   */
  public static void sendHtml(final String subject, final String body, final EmailAttachment attachment, final String... recipients) {
    getExecutorService().execute(getSendHtmlRunable(subject, body, attachment, recipients));
  }


  private static Runnable getSendHtmlRunable(final String subject, final String body, final EmailAttachment attachment, final String... recipients) {
    return new Runnable() {
      @Override
      public void run() {
        try {
          Mailer.sendHtml(subject, body, attachment, recipients);
        } catch (EmailException e) {
          e.printStackTrace();
        }
      }
    };
  }

  /**
   * @param subject    主题
   * @param body       内容
   * @param attachment 附件
   * @param recipients 收件人
   */
  public static void sendAttachment(final String subject, final String body, final EmailAttachment attachment, final String... recipients) {
    getExecutorService().execute(getSendAttachRunnable(subject, body, attachment, recipients));
  }

  private static Runnable getSendAttachRunnable(final String subject, final String body, final EmailAttachment attachment, final String... recipients) {
    return new Runnable() {
      @Override
      public void run() {
        try {
          Mailer.sendAttachment(subject, body, attachment, recipients);
        } catch (EmailException e) {
          e.printStackTrace();
        }
      }
    };
  }

  public void shutdown() {
    getExecutorService().shutdown();
  }

  public List<Runnable> shutdownNow() {
    return getExecutorService().shutdownNow();
  }
}
