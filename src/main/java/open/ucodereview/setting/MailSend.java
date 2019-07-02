package open.ucodereview.setting;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class MailSend {

  private final static String mailTimeout = "20000";
  private static String PASSWORD = null;
  private static String EMAILFROM = null;

  private static Properties prop = new Properties();

  static {
    Properties p = new Properties();
    try {
      p.load(MailSend.class.getClassLoader().getResourceAsStream("mail.properties"));
      String HOST = p.getProperty("mail.smtpHost", "smtp.ucarinc.com");
      Integer PORT = Integer.valueOf(p.getProperty("mail.port", "587"));
      PASSWORD = p.getProperty("mail.smtpPassword", "");
      EMAILFROM = p.getProperty("mail.addressFrom", "");

      prop.put("mail.host", HOST);
      prop.put("mail.port", PORT);
      prop.put("mail.transport.protocol", "smtp");
      prop.put("mail.smtp.auth", "true");
      prop.setProperty("mail.smtp.timeout", mailTimeout);
      prop.setProperty("mail.smtp.auth", "true");
      prop.setProperty("mail.smtp.starttls.enable", "true");
      prop.setProperty("mail.smtp.ssl.enable", "false");
      prop.setProperty("mail.smtp.ssl.trust", HOST);
    } catch (IOException var2) {
      var2.printStackTrace();
    }
  }

  public static void sendMail(String title, List<String> emailList, String content,
      String attachPath) throws Exception {
    Session session = Session.getInstance(prop);
    MimeMessage message = new MimeMessage(session);
    message.setFrom(new InternetAddress(EMAILFROM));
    message.setRecipients(Message.RecipientType.TO, getReceivers(emailList));
    message.setSubject(title);
    message.setContent(content, "text/html;charset=utf-8");

    // // 文本部分
    MimeBodyPart textPart = new MimeBodyPart();
    textPart.setContent(content, "text/html;charset=UTF-8");

    // 附件部分
    MimeBodyPart attachmentPart = new MimeBodyPart();
    DataHandler dh = new DataHandler(new FileDataSource(attachPath));// 附件路径
    String fileName = dh.getName();
    attachmentPart.setDataHandler(dh);
    attachmentPart.setFileName(fileName);
    // 图文和附件整合，复杂关系
    MimeMultipart mmp = new MimeMultipart();
    mmp.addBodyPart(textPart);
    mmp.addBodyPart(attachmentPart);
    mmp.setSubType("mixed");
    // 将以上内容添加到邮件的内容中并确认
    message.setContent(mmp);
    message.saveChanges();

    // 发送邮件
    Transport ts = session.getTransport();
    ts.connect(EMAILFROM, PASSWORD);
    ts.sendMessage(message, message.getAllRecipients());
    ts.close();
  }

  private static Address[] getReceivers(List<String> emailList) throws AddressException {
    Address[] arr = new Address[emailList.size()];
    for (int i = 0; i < arr.length; i++) {
      arr[i] = new InternetAddress(emailList.get(i));
    }
    return arr;
  }

  public static void main(String[] args) throws Exception {
    //sendMail("吸引力注册邮件", "xxx@qq.com", "您的注册验证码为:<b style=\"color:blue;\">651899</b>");
    sendMail("邮件测试2", Arrays.asList("529801034@qq.com", "nengcai.wang@ucarinc.com"), "插件发邮件测试",
        "D:/CodeReview20190610141504154.xls");
  }
}