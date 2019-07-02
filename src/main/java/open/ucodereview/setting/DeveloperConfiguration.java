package open.ucodereview.setting;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import javax.swing.JComponent;
import open.ucodereview.setting.messages.MessageBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

public class DeveloperConfiguration implements Configurable {

  private final Project project;
  private SettingsPanel panel;

  public DeveloperConfiguration(Project project) {
    this.project = project;
  }

  @Nls
  @Override
  public String getDisplayName() {
    return MessageBundle.message("configuration.display.name");
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    if (panel == null) {
      panel = new SettingsPanel(project);
    }
    return panel;
  }

  @Override
  public boolean isModified() {
    return panel != null && panel.isModified();
  }

  @Override
  public void apply() {
    if (panel != null) {
      panel.apply();
    }
  }

  @Override
  public void reset() {
    if (panel != null) {
      panel.reset();
    }
  }

  @Override
  public void disposeUIResources() {
    panel = null;
  }
}
