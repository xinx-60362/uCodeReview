//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package open.ucodereview.data;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import java.util.List;
import java.util.stream.Collectors;
import open.ucodereview.entity.ReviewInfo;
import open.ucodereview.view.component.ButtonGroupComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import open.ucodereview.data.state.State;
import open.ucodereview.setting.Developer;

@com.intellij.openapi.components.State(
    name = "UCodeReview",
    storages = {@Storage("uCodeReview.xml")}
)
public class ReviewDataService implements
    PersistentStateComponent<State> {

  private State myState = new State();
  private ButtonGroupComponent buttonGroupComponent;
  private boolean changed;
  private static Project project;

  public void setLeftButtonsView(ButtonGroupComponent buttonGroupComponent) {
    this.buttonGroupComponent = buttonGroupComponent;
  }

  private ReviewDataService() {
  }

  @NotNull
  public static ReviewDataService getInstance(Project project) {
    ReviewDataService.project = project;
    ReviewDataService service = ServiceManager
        .getService(project, ReviewDataService.class);
    if (service == null) {
      throw new RuntimeException("ReviewDataService is null");
    }

    return service;
  }

  public void resourseUpdated(ReviewInfo reviewInfo) {
    this.myState.recordDevelopers(reviewInfo);
  }

  public ReviewInfo getResourse(String id) {
    return this.myState.getCodeReviewInfoByKey(id);
  }

  public void removeResourse(String key) {
    this.myState.removeCodeReviewInfoById(key);
  }

  public void cacheUpdated(ReviewInfo reviewInfo) {
    this.myState.recordLastCodeReviewInfoToCache(reviewInfo);
  }

  public ReviewInfo getResourseFromCache() {
    return this.myState.getCacheReviewInfo();
  }

  public List<String> getCheckUsers() {
    return this.myState.getCheckUsers();
  }

  @Override
  @Nullable
  public State getState() {
    if (changed && buttonGroupComponent != null) {
      buttonGroupComponent.refreshList();
      changed = false;
    }
    return this.myState;
  }

  @Override
  public void loadState(@NotNull State state) {
    if (state == null) {
      throw new RuntimeException("state is null");
    }
    if (buttonGroupComponent != null) {
      changed = true;
    }
    this.myState = state;
  }

  public ReviewInfo getRvInfo(String id) {
    return getResourse(id);
  }

  public void changeResourseStatus(String id, int status) {
    ReviewInfo reviewInfo = getResourse(id);
    reviewInfo.setStatus(status);
    resourseUpdated(reviewInfo);
  }

  public List<ReviewInfo> getAllResourse() {
    return this.myState.getCodeReviewInfoListByStatus(ButtonGroupComponent.STATUS_ALL);
  }

  public List<ReviewInfo> getResourseByStatus(String status) {
    List<ReviewInfo> codeReviewInfoListByStatus = this.myState
        .getCodeReviewInfoListByStatus(status);
    return codeReviewInfoListByStatus.stream()
        .filter(c -> c.getProjectName().equals(project.getName()))
        .collect(Collectors.toList());
  }

  public void saveDevelopers(List<Developer> developers) {
    this.myState.recordDevelopers(developers);
  }

  public List<Developer> getDevelopers() {
    return this.myState.getDevelopers();
  }
}
