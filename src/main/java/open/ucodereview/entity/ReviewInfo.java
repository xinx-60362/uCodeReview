package open.ucodereview.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.util.xmlb.annotations.Tag;
import java.io.Serializable;
import lombok.Data;
import open.ucodereview.view.component.ButtonGroupComponent;
import org.jetbrains.annotations.NotNull;

@Tag("reviewInfo")
@Data
public class ReviewInfo
    implements Serializable, Comparable {

  private static final long serialVersionUID = -6208512095183683437L;
  private static final Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

  @Tag
  private String id;
  @Tag
  private String checkUser;
  @Tag
  private String toCheckUser;
  @Tag
  private String defectDesc;
  @Tag
  private String defectType;
  @Tag
  private String reviesDesc;
  @Tag
  private String projectName;
  @Tag
  private String fileDetailPath;
  @Tag
  private String selectedContext;
  @Tag
  private String opraTime;
  @Tag
  private Integer startLineNumber;
  @Tag
  private Integer endLineNumber;
  @Tag
  private int status = 0;//默认没解决, 1:已解决
  @Tag
  /* 缺陷等级(紧急/重要/一般) */
  private String defectLevel;

  public ReviewInfo() {
  }

  public String toString() {
    return gson.toJson(this);
  }

  @Override
  public int compareTo(@NotNull Object o) {
    if (o == null || !(o instanceof ReviewInfo)) {
      return 0;
    }
    return this.getOpraTime().compareTo(((ReviewInfo) o).getOpraTime());
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getStatusName() {
    return this.status == 0 ? ButtonGroupComponent.STATUS_UNRESOLVED : ButtonGroupComponent.STATUS_RESOLVED;
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ReviewInfo) {
      if (((ReviewInfo) obj).getId().equals(this.id)) {
        return true;
      }
    }
    return super.equals(obj);
  }
}
