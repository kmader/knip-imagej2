package org.knime.knip.imagej2.interactive.plugins;

import imagej.command.CommandInfo;
import imagej.command.CommandModule;
import imagej.module.Module;
import imagej.module.event.ModuleEvent;
import imagej.module.event.ModuleExecutedEvent;

import java.util.ArrayList;
import java.util.List;

import org.scijava.event.EventHandler;
import org.scijava.event.SciJavaEvent;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;

@Plugin(type = KNIMERecorder.class)
public class KNIMERecorder extends AbstractService {

    private List<CommandInfo> moduleInfos = new ArrayList<CommandInfo>();

    public KNIMERecorder() {

    }

    @EventHandler
    protected void onEvent(final SciJavaEvent evt) {
        if (ModuleEvent.class.isAssignableFrom(evt.getClass())) {
            if (evt instanceof ModuleExecutedEvent) {
                Module module = ((ModuleExecutedEvent)evt).getModule();
                if (module instanceof CommandModule && module.getInfo().canRunHeadless()) {
                    moduleInfos.add((CommandInfo)module.getInfo());
                }
            }
        }
    }

    public List<CommandInfo> moduleInfos() {
        return this.moduleInfos;
    }
}