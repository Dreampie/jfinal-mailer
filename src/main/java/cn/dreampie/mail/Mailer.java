package cn.dreampie.mail;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Mailer.sendHtml("测试","173956022@qq.com","<a href='www.dreampie.cn'>Dreampie</a>");
 * Created by wangrenhui on 14-5-6.
 */
public class Mailer {
  private static Logger logger = LoggerFactory.getLogger(Mailer.class);

  /**
   * @param subject    主题
   * @param body       内容
   * @param recipients 收件人
   */
  public static void sendText(String subject, String body, String... recipients) {
    try {
      SimpleEmail simpleEmail = getSimpleEmail(subject, body, recipients);
      simpleEmail.send();
      logger.info("send email to {}", StringUtils.join(recipients));
    } catch (EmailException e) {
      throw new RuntimeException("Unabled to send email", e);
    }
  }

  public static SimpleEmail getSimpleEmail(String subject, String body, String... recipients) throws EmailException {
    SimpleEmail simpleEmail = new SimpleEmail();
    configEmail(subject, simpleEmail, recipients);
    simpleEmail.setMsg(body);
    return simpleEmail;
  }


  /**
   * @param subject    主题
   * @param body       内容
   * @param recipients 收件人
   */
  public static void sendHtml(String subject, String body, String... recipients) {
    sendHtml(subject, body, null, recipients);
  }

  /**
   * @param subject    主题
   * @param body       内容
   * @param attachment 附件
   * @param recipients 收件人
   */
  public static void sendHtml(String subject, String body, EmailAttachment attachment, String... recipients) {
    try {
      HtmlEmail htmlEmail = getHtmlEmail(subject, body, attachment, recipients);
      htmlEmail.send();
      logger.info("send email to {}", StringUtils.join(recipients));
    } catch (EmailException e) {
      throw new RuntimeException("Unabled to send email", e);
    }
  }

  public static HtmlEmail getHtmlEmail(String subject, String body, String... recipients) {
    return getHtmlEmail(subject, body, null, recipients);
  }

  public static HtmlEmail getHtmlEmail(String subject, String body, EmailAttachment attachment, String... recipients) {
    try {
      HtmlEmail htmlEmail = new HtmlEmail();
      configEmail(subject, htmlEmail, recipients);
      htmlEmail.setHtmlMsg(body);
      // set the alter native message
      htmlEmail.setTextMsg("Your email client does not support HTML messages");
      if (attachment != null)
        htmlEmail.attach(attachment);
      return htmlEmail;
    } catch (EmailException e) {
      throw new RuntimeException("Unabled to send email", e);
    }
  }

  /**
   * @param subject    主题
   * @param body       内容
   * @param attachment 附件
   * @param recipients 收件人
   */
  public static void sendAttachment(String subject, String body, EmailAttachment attachment, String... recipients) {
    try {
      MultiPartEmail multiPartEmail = getMultiPartEmail(subject, body, attachment, recipients);
      multiPartEmail.send();
      logger.info("send email to {}", StringUtils.join(recipients));
    } catch (EmailException e) {
      throw new RuntimeException("Unabled to send email", e);
    }
  }

  public static MultiPartEmail getMultiPartEmail(String subject, String body, String... recipients) {
    return getMultiPartEmail(subject, body, null, recipients);
  }

  public static MultiPartEmail getMultiPartEmail(String subject, String body, EmailAttachment attachment, String... recipients) {
    try {
      MultiPartEmail multiPartEmail = new MultiPartEmail();
      configEmail(subject, multiPartEmail, recipients);
      multiPartEmail.setMsg(body);
      // add the attachment
      if (attachment != null)
        multiPartEmail.attach(attachment);
      return multiPartEmail;
    } catch (EmailException e) {
      throw new RuntimeException("Unabled to send email", e);
    }
  }

  public static void configEmail(String subject, Email email, String... recipients) throws EmailException {

    if (recipients == null)
      throw new EmailException("Recipients not found.");
    MailerConf mailerConf = MailerPlugin.mailerConf;
    email.setCharset(mailerConf.getCharset());
    email.setSocketTimeout(mailerConf.getTimeout());
    email.setSocketConnectionTimeout(mailerConf.getConnectout());
    email.setCharset(mailerConf.getEncode());
    email.setHostName(mailerConf.getHost());
    if (!mailerConf.getSslport().isEmpty())
      email.setSslSmtpPort(mailerConf.getSslport());
    if (!mailerConf.getPort().isEmpty())
      email.setSmtpPort(Integer.parseInt(mailerConf.getPort()));
    email.setSSLOnConnect(mailerConf.isSsl());
    email.setStartTLSEnabled(mailerConf.isTls());
    email.setDebug(mailerConf.isDebug());
    email.setAuthentication(mailerConf.getUser(), mailerConf.getPassword());
    email.setFrom(mailerConf.getFrom(), mailerConf.getName());
    email.setSubject(subject);
    email.addTo(recipients);
  }

}
