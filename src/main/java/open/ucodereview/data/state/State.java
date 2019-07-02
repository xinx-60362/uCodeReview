package open.ucodereview.data.state;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xmlb.annotations.MapAnnotation;
import com.intellij.util.xmlb.annotations.Tag;
import com.intellij.util.xmlb.annotations.XCollection;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import open.ucodereview.entity.ReviewInfo;
import open.ucodereview.setting.Developer;
import open.ucodereview.view.component.ButtonGroupComponent;

public class State implements Serializable {
  private static final Gson gson = (new GsonBuilder()).enableComplexMapKeySerialization().create();

  @XCollection(propertyElementName = "reviewInfoList")
  private Set<ReviewInfo> codeReviewInfoMap = new CopyOnWriteArraySet<>();

  @Tag("cacheLastReviewInfo")
  @MapAnnotation(
      entryTagName = "cacheReviewInfo",
      keyAttributeName = "id",
      valueAttributeName = "infoToJson",
      surroundWithTag = false,
      surroundKeyWithTag = false,
      surroundValueWithTag = false
  )
  private Map<String, String> cacheLastReviewInfoMap = ContainerUtil.newHashMap();

  @Tag("checkUserList")
  @MapAnnotation(
      entryTagName = "user",
      keyAttributeName = "name",
      surroundWithTag = false,
      surroundKeyWithTag = false,
      surroundValueWithTag = false
  )
  private Map<String, String> checkUserMap = ContainerUtil.newHashMap(); // 保留检查人和被检查人用户

  @XCollection(propertyElementName = "developers")
  private List<Developer> developers = new ArrayList<>();


  public State() {

  }

  public void recordDevelopers(ReviewInfo reviewInfo) {
    this.codeReviewInfoMap.add(reviewInfo);
  }

  public ReviewInfo getCodeReviewInfoByKey(String key) {
    if (StringUtil.isNotEmpty(key)) {
      for (ReviewInfo entity : codeReviewInfoMap) {
        if (entity.getId().equals(key)) {
          return entity;
        }
      }
    }
    return null;
  }

  public void removeCodeReviewInfoById(String key) {
    if (StringUtil.isNotEmpty(key)) {
      for (ReviewInfo entity : codeReviewInfoMap) {
        if (entity.getId().equals(key)) {
          codeReviewInfoMap.remove(entity);
        }
      }
    } else {
      this.codeReviewInfoMap.clear();
    }
  }

  public void recordLastCodeReviewInfoToCache(ReviewInfo reviewInfo) {
    this.cacheLastReviewInfoMap.clear();
    this.cacheLastReviewInfoMap
        .put(reviewInfo.getId(), reviewInfo.toString());

    String checkUser = reviewInfo.getCheckUser();
    String toCheckUser = reviewInfo.getToCheckUser();
    if (!checkUserMap.containsKey(checkUser)) {
      checkUserMap.put(checkUser, checkUser);
    }
    if (!checkUserMap.containsKey(toCheckUser)) {
      checkUserMap.put(toCheckUser, toCheckUser);
    }
  }

  public ReviewInfo getCacheReviewInfo() {
    if (this.cacheLastReviewInfoMap.isEmpty()) {
      return null;
    } else {
      ReviewInfo reviewInfo = null;
      Iterator var2 = this.cacheLastReviewInfoMap.entrySet().iterator();
      if (var2.hasNext()) {
        Entry<String, String> entry = (Entry) var2.next();
        reviewInfo = gson
            .fromJson(entry.getValue(), ReviewInfo.class);
      }

      return reviewInfo;
    }
  }

  public List<String> getCheckUsers() {
    return new ArrayList<>(checkUserMap.keySet());
  }

  public List<ReviewInfo> getCodeReviewInfoListByStatus(String status) {
    List<ReviewInfo> codeReviewInfoEntities = new ArrayList();
    for (ReviewInfo reviewInfo : codeReviewInfoMap) {
      boolean flag = false;
      if (status.equals(ButtonGroupComponent.STATUS_ALL)) {
        flag = true;
      } else if (status.equals(ButtonGroupComponent.STATUS_RESOLVED)) {
        flag = reviewInfo.getStatus() == 1;
      } else if (status.equals(ButtonGroupComponent.STATUS_UNRESOLVED)) {
        flag = reviewInfo.getStatus() == 0;
      }

      if (flag) {
        codeReviewInfoEntities.add(reviewInfo);
      }
    }

    return codeReviewInfoEntities;
  }

  public List<Developer> getDevelopers() {
    return developers;
  }

  public void recordDevelopers(List<Developer> developers) {
    this.developers = developers;
  }
}