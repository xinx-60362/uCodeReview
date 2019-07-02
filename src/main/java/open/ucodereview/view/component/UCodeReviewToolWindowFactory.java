//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package open.ucodereview.view.component;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory.SERVICE;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

public class UCodeReviewToolWindowFactory implements ToolWindowFactory {

  public UCodeReviewToolWindowFactory() {
  }

  public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
    if (project == null) {
      throw new IllegalArgumentException("project is null");
    }

    if (toolWindow == null) {
      throw new IllegalArgumentException("toolWindow is null");
    }

    ContentManager contentManager = toolWindow.getContentManager();
    ShowReviewInfoListView showReviewInfoListView = new ShowReviewInfoListView(project);
    Content content = SERVICE.getInstance()
        .createContent(showReviewInfoListView, "Show Review", false);
    content.setCloseable(false);
    contentManager.addContent(content);
  }
}
