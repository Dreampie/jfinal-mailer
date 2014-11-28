package cn.dreampie.mail;

import akka.actor.Scheduler;
import cn.dreampie.akka.Akka;
import org.apache.commons.mail.*;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * AkkaMailer.sendHtml("测试","173956022@qq.com","<a href='www.dreampie.cn'>Dreampie</a>");
 * Created by wangrenhui on 14-5-6.
 */
public class AkkaMailer {

  private static Scheduler scheduler = null;

  public static Scheduler getScheduler() {
    if (scheduler == null)
      scheduler = Akka.system().scheduler();
    return scheduler;
  }

  public static void setScheduler(Scheduler scheduler) {
    AkkaMailer.scheduler = scheduler;
  }

  /**
   * @param subject    主题
   * @param body       内容
   * @param recipients 收件人
   */
  public static void sendText(final String subject, final String body, final String... recipients) {
    getScheduler().scheduleOnce(Duration.create(1000, TimeUnit.MILLISECONDS),
        getSendTextRunnable(subject, body, recipients), Akka.system().dispatcher());
  }

  private static Runnable getSendTextRunnable(final String subject, final String body, final String... recipients) {
    return new Runnable() {
      @Override
      public void run() {
        Mailer.sendText(subject, body, recipients);
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
    getScheduler().scheduleOnce(Duration.create(1000, TimeUnit.MILLISECONDS),
        getSendHtmlRunable(subject, body, attachment, recipients), Akka.system().dispatcher());
  }

  private static Runnable getSendHtmlRunable(final String subject, final String body, final EmailAttachment attachment, final String... recipients) {
    return new Runnable() {
      @Override
      public void run() {
        Mailer.sendHtml(subject, body, attachment, recipients);
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
    getScheduler().scheduleOnce(Duration.create(1000, TimeUnit.MILLISECONDS),
        getSendAttachmentRunnable(subject, body, attachment, recipients), Akka.system().dispatcher());
  }

  private static Runnable getSendAttachmentRunnable(final String subject, final String body, final EmailAttachment attachment, final String... recipients) {
    return new Runnable() {
      @Override
      public void run() {
        Mailer.sendAttachment(subject, body, attachment, recipients);
      }
    };
  }
}
