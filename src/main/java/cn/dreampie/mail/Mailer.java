package cn.dreampie.mail;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Mailer.me().sendHtml("测试","173956022@qq.com","<a href='www.dreampie.cn'>Dreampie</a>");
 * Created by wangrenhui on 14-5-6.
 */
public class Mailer {
  private static Logger logger = LoggerFactory.getLogger(Mailer.class);

  private static ExecutorService executorService = null;

  public static ExecutorService getExecutorService() {
    if (executorService == null)
      executorService = Executors.newCachedThreadPool();
    return executorService;
  }

  public static void setExecutorService(ExecutorService executorService) {
    Mailer.executorService = executorService;
  }

  /**
   * @param subject    主题
   * @param body       内容
   * @param recipients 收件人
   */
  public static void sendHtml(final String subject, final String body, final String... recipients) {
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

//  public static void sendHtmlByAkka(final String subject, final String body, final EmailAttachment attachment, final String... recipients) {
//
//    Akka.system().scheduler().scheduleOnce(Duration.create(1000, TimeUnit.MILLISECONDS),
//        getSendHtmlRunable(subject, body, attachment, recipients), Akka.system().dispatcher());
//  }

  private static Runnable getSendHtmlRunable(final String subject, final String body, final EmailAttachment attachment, final String[] recipients) {
    return new Runnable() {
      @Override
      public void run() {
        MailerConf mailerConf = MailerPlugin.mailerConf;
        HtmlEmail htmlEmail = new HtmlEmail();
        htmlEmail.setCharset(mailerConf.getCharset());
        htmlEmail.setSocketTimeout(mailerConf.getTimeout());
        htmlEmail.setCharset(mailerConf.getEncode());
        htmlEmail.setHostName(mailerConf.getHost());
        if (!mailerConf.getSslport().isEmpty())
          htmlEmail.setSslSmtpPort(mailerConf.getSslport());
        if (!mailerConf.getPort().isEmpty())
          htmlEmail.setSmtpPort(Integer.parseInt(mailerConf.getPort()));
        htmlEmail.setSSLOnConnect(mailerConf.isSsl());
        htmlEmail.setStartTLSEnabled(mailerConf.isTls());
        htmlEmail.setDebug(mailerConf.isDebug());
        htmlEmail.setAuthenticator(new DefaultAuthenticator(mailerConf.getUser(), mailerConf.getPassword()));
        try {
          htmlEmail.setFrom(mailerConf.getFrom(), mailerConf.getName());
          htmlEmail.setSubject(subject);
          htmlEmail.addTo(recipients);
          htmlEmail.setHtmlMsg(body);
          // set the alternative message
          htmlEmail.setTextMsg("Your email client does not support HTML messages");
          if (attachment != null)
            htmlEmail.attach(attachment);
          htmlEmail.send();
          logger.info("send email to {}", StringUtils.join(recipients));
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
  public static void sendText(final String subject, final String body, final String... recipients) {
    getExecutorService().execute(getSendTextRunnable(subject, body, recipients));
  }

//  public static void sendTextByAkka(final String subject, final String body, final String... recipients) {
//    Akka.system().scheduler().scheduleOnce(Duration.create(1000, TimeUnit.MILLISECONDS),
//        getSendTextRunnable(subject, body, recipients), Akka.system().dispatcher());
//  }

  private static Runnable getSendTextRunnable(final String subject, final String body, final String[] recipients) {
    return new Runnable() {
      @Override
      public void run() {
        MailerConf mailerConf = MailerPlugin.mailerConf;
        SimpleEmail simpleEmail = new SimpleEmail();
        simpleEmail.setCharset(mailerConf.getCharset());
        simpleEmail.setSocketTimeout(mailerConf.getTimeout());
        simpleEmail.setCharset(mailerConf.getEncode());
        simpleEmail.setHostName(mailerConf.getHost());
        if (!mailerConf.getSslport().isEmpty())
          simpleEmail.setSslSmtpPort(mailerConf.getSslport());
        if (!mailerConf.getPort().isEmpty())
          simpleEmail.setSmtpPort(Integer.parseInt(mailerConf.getPort()));
        simpleEmail.setSSLOnConnect(mailerConf.isSsl());
        simpleEmail.setStartTLSEnabled(mailerConf.isTls());
        simpleEmail.setDebug(mailerConf.isDebug());
        simpleEmail.setAuthentication(mailerConf.getUser(), mailerConf.getPassword());
        try {
          simpleEmail.setFrom(mailerConf.getFrom(), mailerConf.getName());
          simpleEmail.setSubject(subject);
          simpleEmail.addTo(recipients);
          simpleEmail.setMsg(body);
          simpleEmail.send();
          logger.info("send email to {}", StringUtils.join(recipients));
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

//  public static void sendAttachmentByAkka(final String subject, final String body, final EmailAttachment attachment, final String... recipients) {
//
//    Akka.system().scheduler().scheduleOnce(Duration.create(1000, TimeUnit.MILLISECONDS),
//        getSendAttachRunnable(subject, body, attachment, recipients), Akka.system().dispatcher());
//  }

  private static Runnable getSendAttachRunnable(final String subject, final String body, final EmailAttachment attachment, final String[] recipients) {
    return new Runnable() {
      @Override
      public void run() {
        MailerConf mailerConf = MailerPlugin.mailerConf;
        MultiPartEmail multiPartEmail = new MultiPartEmail();
        multiPartEmail.setCharset(mailerConf.getCharset());
        multiPartEmail.setSocketTimeout(mailerConf.getTimeout());
        multiPartEmail.setCharset(mailerConf.getEncode());
        multiPartEmail.setHostName(mailerConf.getHost());
        if (!mailerConf.getSslport().isEmpty())
          multiPartEmail.setSslSmtpPort(mailerConf.getSslport());
        if (!mailerConf.getPort().isEmpty())
          multiPartEmail.setSmtpPort(Integer.parseInt(mailerConf.getPort()));
        multiPartEmail.setSSLOnConnect(mailerConf.isSsl());
        multiPartEmail.setStartTLSEnabled(mailerConf.isTls());
        multiPartEmail.setDebug(mailerConf.isDebug());
        multiPartEmail.setAuthentication(mailerConf.getUser(), mailerConf.getPassword());
        try {
          multiPartEmail.setFrom(mailerConf.getFrom(), mailerConf.getName());
          multiPartEmail.setSubject(subject);
          multiPartEmail.addTo(recipients);
          multiPartEmail.setMsg(body);
          // add the attachment
          if (attachment != null)
            multiPartEmail.attach(attachment);
          multiPartEmail.send();
          logger.info("send email to {}", StringUtils.join(recipients));
        } catch (EmailException e) {
          e.printStackTrace();
        }
      }
    };
  }
}
