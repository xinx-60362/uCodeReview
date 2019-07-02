package open.ucodereview.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import open.ucodereview.view.AddCodeReviewView;

public class AddCodeReviewAction
    extends AnAction {

  public void actionPerformed(AnActionEvent event) {
    Project project = event.getData(PlatformDataKeys.PROJECT);
    Editor editor = event.getData(PlatformDataKeys.EDITOR);
    if ((null == project) || (null == editor)) {
      return;
    }
    PsiFile mFile = PsiUtilBase.getPsiFileInEditor(editor, project);
    new AddCodeReviewView(project, editor, mFile).showAndGet();
  }
}
