package Editor;

import Observers.EventSystem;
import Observers.Events.Event;
import Observers.Events.EventType;

import imgui.ImGui;

public class MenuBar {
    public void imGui() {
        ImGui.beginMainMenuBar();

        if (ImGui.beginMenu("File")) {
            if (ImGui.menuItem("Save", "Ctrl+S")) { //TODO Implement Shortcut
                EventSystem.notify(null, new Event(EventType.SaveLevel));
            }

            if (ImGui.menuItem("Load", "Ctrl+O")) {
                EventSystem.notify(null, new Event(EventType.LoadLevel));
            }

            ImGui.endMenu();
        }

        ImGui.endMainMenuBar();
    }
}
