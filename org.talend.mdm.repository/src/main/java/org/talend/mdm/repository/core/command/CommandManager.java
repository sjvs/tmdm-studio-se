// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.mdm.repository.core.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.IMementoAware;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.mdm.repository.core.command.deploy.AbstractDeployCommand;
import org.talend.mdm.repository.core.command.deploy.AddCommand;
import org.talend.mdm.repository.core.command.deploy.DeleteCommand;
import org.talend.mdm.repository.core.command.deploy.DeployCompoundCommand;
import org.talend.mdm.repository.core.command.deploy.ModifyCommand;
import org.talend.mdm.repository.core.command.deploy.RenameCommand;
import org.talend.mdm.repository.core.command.deploy.job.BatchDeployJobCommand;
import org.talend.mdm.repository.core.command.impl.NOPCommand;
import org.talend.mdm.repository.core.command.impl.RestoreCommand;
import org.talend.mdm.repository.model.mdmmetadata.MDMServerDef;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC hbhong class global comment. Detailled comment
 */
public class CommandManager implements IMementoAware {

    /**
     * 
     */

    private static CommandManager instance = new CommandManager();

    static Logger log = Logger.getLogger(CommandManager.class);

    public static CommandManager getInstance() {
        return instance;
    }

    IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();

    private Map<String, CommandStack> map = new HashMap<String, CommandStack>();

    public CommandStack findCommandStack(String id) {
        if (id == null)
            return null;
        return map.get(id);
    }

    public ICommand getNewCommand(int type) {
        switch (type) {
        case ICommand.CMD_NOP:
            return new NOPCommand();
        case ICommand.CMD_ADD:
            return new AddCommand();
        case ICommand.CMD_DELETE:
            return new DeleteCommand();

        case ICommand.CMD_MODIFY:
            return new ModifyCommand();
        case ICommand.CMD_RENAME:
            return new RenameCommand();
        case ICommand.CMD_RESTORE:
            return new RestoreCommand();
        }
        return null;
    }

    public ICommand copyCommand(ICommand cmd, Object param) {
        ICommand newCmd = getNewCommand(cmd.getCommandType());

        if (param == null) {
            param = new String[] { cmd.getObjName(), cmd.getObjLastName() };
        }
        newCmd.init(cmd.getCommandId(), param);
        return newCmd;
    }

    public ICommand convertToDeployCompoundCommand(AbstractDeployCommand cmd) {
        CompoundCommand compoundCommand = new DeployCompoundCommand(cmd);
        compoundCommand.setCommandType(cmd.getCommandType());
        compoundCommand.init();
        if (compoundCommand.breakUpCommand()) {
            return compoundCommand;
        }
        return cmd;
    }

    public void fillViewObjectToCommand(ICommand cmd) {
        try {
            IRepositoryViewObject viewObject = factory.getLastVersion(cmd.getCommandId());
            cmd.updateViewObject(viewObject);
        } catch (PersistenceException e) {
            log.error(e.getMessage(), e);
        }
    }

    ICommand nopCommand;

    public ICommand getDefaultNOPCommand() {
        if (nopCommand == null) {
            nopCommand = getNewCommand(ICommand.CMD_NOP);
        }
        return nopCommand;
    }

    public void pushCommand(int type, IRepositoryViewObject viewObj) {
        ICommand newCommand = getNewCommand(type);
        newCommand.init(viewObj);
        pushCommand(newCommand);
    }

    public void pushCommand(int type, String id, Object name) {
        ICommand newCommand = getNewCommand(type);
        newCommand.init(id, name);
        pushCommand(newCommand);
    }

    private void pushCommand(ICommand command) {
        CommandStack commandStack = map.get(command.getCommandId());
        if (commandStack == null) {
            commandStack = new CommandStack();
            map.put(command.getCommandId(), commandStack);
        }
        commandStack.pushCommand(command);
    }

    public ICommand restoreCommand(IMemento mem) {
        int type = mem.getInteger(ICommand.PROP_TYPE);
        ICommand cmd = null;
        cmd = getNewCommand(type);
        if (cmd != null) {
            cmd.restoreState(mem);
        }
        return cmd;
    }

    public void removeCommandStack(String id) {
        if (id == null)
            return;
        map.remove(id);
    }

    public void restoreState(IMemento aMemento) {
        if (map.isEmpty()) {
            IMemento cmdManagerMem = aMemento.getChild(ICommand.MDM_COMMANDS);
            if (cmdManagerMem != null) {
                IMemento[] stackMems = cmdManagerMem.getChildren(ICommand.MEM_TYPE_COMMAND_STACK);
                if (stackMems != null) {
                    for (IMemento stackMem : stackMems) {
                        CommandStack stack = new CommandStack();
                        stack.restoreState(stackMem);
                        if (stack.getCommandId() != null) {
                            map.put(stack.getCommandId(), stack);
                        }
                    }
                }

            }
        }
    }

    public void saveState(IMemento aMemento) {
        IMemento cmdManagerMem = aMemento.createChild(ICommand.MDM_COMMANDS);
        for (CommandStack stack : map.values()) {
            try {
                if (factory.getLastVersion(stack.getCommandId()) != null) {
                    IMemento stackMem = cmdManagerMem.createChild(ICommand.MEM_TYPE_COMMAND_STACK);
                    stack.saveState(stackMem);
                }
            } catch (PersistenceException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public List<AbstractDeployCommand> getAllDeployCommands() {
        List<AbstractDeployCommand> cmds = new ArrayList<AbstractDeployCommand>();
        for (CommandStack stack : map.values()) {
            ICommand validCommand = stack.getValidCommand();
            if (validCommand != null) {
                if (validCommand instanceof AbstractDeployCommand) {
                    fillViewObjectToCommand(validCommand);
                    AbstractDeployCommand deployCommand = (AbstractDeployCommand) validCommand;
                    cmds.add(deployCommand);
                }
            }
        }
        return cmds;
    }

    public List<AbstractDeployCommand> getDeployCommands(List<IRepositoryViewObject> viewObjs) {
        List<AbstractDeployCommand> cmds = new LinkedList<AbstractDeployCommand>();
        for (IRepositoryViewObject viewObj : viewObjs) {
            CommandStack stack = findCommandStack(viewObj.getId());
            if (stack == null) {
                stack = new CommandStack();
                ICommand cmd = getNewCommand(ICommand.CMD_MODIFY);
                cmd.init(viewObj);
                stack.pushCommand(cmd);
            }
            ICommand validCommand = stack.getValidCommand();
            if (validCommand != null) {
                if (validCommand instanceof AbstractDeployCommand) {
                    fillViewObjectToCommand(validCommand);
                    AbstractDeployCommand deployCommand = (AbstractDeployCommand) validCommand;
                    cmds.add(deployCommand);
                }
            }
        }
        return cmds;
    }

    public List<ICommand> convertToDeployCompundCommands(List<AbstractDeployCommand> validCommands, MDMServerDef serverDef) {
        List<ICommand> cmds = new ArrayList<ICommand>(validCommands.size());
        for (AbstractDeployCommand deployCommand : validCommands) {
            deployCommand.setServerDef(serverDef);
            ICommand newCmd = convertToDeployCompoundCommand(deployCommand);

            cmds.add(newCmd);
        }
        return cmds;
    }

    public void arrangeForJobCommands(List<ICommand> cmds) {
        BatchDeployJobCommand jobCommand = new BatchDeployJobCommand();

        for (Iterator<ICommand> il = cmds.iterator(); il.hasNext();) {
            ICommand cmd = il.next();
            IRepositoryViewObject viewObject = cmd.getViewObject();
            if (viewObject.getRepositoryObjectType() == ERepositoryObjectType.PROCESS) {
                if (cmd instanceof CompoundCommand) {
                    for (ICommand subCmd : ((CompoundCommand) cmd).getSubCommands()) {
                        int type = subCmd.getCommandType();
                        if (type == ICommand.CMD_DELETE) {
                            jobCommand.addDeleteCommand(subCmd);
                        } else if (type == ICommand.CMD_MODIFY) {
                            jobCommand.addCommand(subCmd);
                        } else {
                            // TODO remove it after debug
                            System.out.println();
                        }
                    }
                } else {
                    jobCommand.addCommand(cmd);
                }
                il.remove();
            }
        }
        if (!jobCommand.isEmpty()) {
            cmds.add(jobCommand);
        }
    }
}
