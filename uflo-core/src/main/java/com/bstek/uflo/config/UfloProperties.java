package com.bstek.uflo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * UFLO工作流引擎配置属性
 *
 * @author Claude
 * @since 2023-01-01
 */
@ConfigurationProperties(prefix = "uflo")
public class UfloProperties {

    /**
     * 是否启用UFLO
     */
    private boolean enabled = true;

    /**
     * 业务日工作小时数
     */
    private int businessDayHours = 8;

    /**
     * ID块大小
     */
    private int idBlockSize = 5000;

    /**
     * 作业线程数
     */
    private int jobThreadCount = 2;

    /**
     * 是否设置调度线程为守护线程
     */
    private String makeSchedulerThreadDaemon = "true";

    /**
     * 是否启用扫描提醒作业
     */
    private boolean enableScanReminderJob = true;

    /**
     * 是否禁用调度器
     */
    private boolean disableScheduler = false;

    /**
     * 集群实例名称
     */
    private String clusterInstanceNames;

    /**
     * 是否禁用用户分配提供者
     */
    private boolean disabledUserAssigneeProvider = false;

    /**
     * 是否禁用部门分配提供者
     */
    private boolean disabledDeptAssigneeProvider = false;

    /**
     * 是否调试模式
     */
    private boolean debug = false;

    /**
     * 是否禁用默认任务图信息提供者
     */
    private boolean disableDefaultTaskDiagramInfoProvider = false;

    /**
     * 默认文件存储目录
     */
    private String defaultFileStoreDir = "/WEB-INF/processfiles";

    /**
     * 是否禁用默认文件流程提供者
     */
    private boolean disableDefaultFileProcessProvider = false;

    /**
     * 图: 是否显示时间
     */
    private boolean diagramShowTime = true;

    /**
     * 图: 已过节点背景色
     */
    private String diagramPassedNodeBgcolor = "245,245,245";

    /**
     * 图: 已过节点字体颜色
     */
    private String diagramPassedNodeFontColor = "150,150,150";

    /**
     * 图: 已过节点字体大小
     */
    private int diagramPassedNodeFontSize = 13;

    /**
     * 图: 已过节点边框颜色
     */
    private String diagramPassedNodeBorderColor = "180,180,180";

    /**
     * 图: 已过连接线颜色
     */
    private String diagramPassedConnectionColor = "180,180,180";

    /**
     * 图: 已过连接线字体颜色
     */
    private String diagramPassedConnectionFontColor = "180,180,180";

    /**
     * 图: 已过连接线字体大小
     */
    private int diagramPassedConnectionFontSize = 12;

    /**
     * 图: 多个当前节点背景色
     */
    private String diagramMultiCurrentNodeBgcolor = "255,255,102";

    /**
     * 图: 多个当前节点字体颜色
     */
    private String diagramMultiCurrentNodeFontColor = "255,102,0";

    /**
     * 图: 多个当前节点字体大小
     */
    private int diagramMultiCurrentNodeFontSize = 13;

    /**
     * 图: 多个当前节点边框颜色
     */
    private String diagramMultiCurrentNodeBorderColor = "255,204,0";

    /**
     * 图: 连接线颜色
     */
    private String diagramConnectionColor = "0,69,123";

    /**
     * 图: 连接线字体颜色
     */
    private String diagramConnectionFontColor = "0,69,123";

    /**
     * 图: 连接线字体大小
     */
    private int diagramConnectionFontSize = 12;

    /**
     * 图: 节点背景色
     */
    private String diagramNodeBgcolor = "255,255,255";

    /**
     * 图: 节点字体颜色
     */
    private String diagramNodeFontColor = "0,0,0";

    /**
     * 图: 节点字体大小
     */
    private int diagramNodeFontSize = 13;

    /**
     * 图: 节点边框颜色
     */
    private String diagramNodeBorderColor = "0,69,123";

    /**
     * 图: 当前节点背景色
     */
    private String diagramCurrentNodeBgcolor = "255,255,204";

    /**
     * 图: 当前节点字体颜色
     */
    private String diagramCurrentNodeFontColor = "255,102,0";

    /**
     * 图: 当前节点字体大小
     */
    private int diagramCurrentNodeFontSize = 13;

    /**
     * 图: 当前节点边框颜色
     */
    private String diagramCurrentNodeBorderColor = "255,204,0";

    // Getters and Setters
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getBusinessDayHours() {
        return businessDayHours;
    }

    public void setBusinessDayHours(int businessDayHours) {
        this.businessDayHours = businessDayHours;
    }

    public int getIdBlockSize() {
        return idBlockSize;
    }

    public void setIdBlockSize(int idBlockSize) {
        this.idBlockSize = idBlockSize;
    }

    public int getJobThreadCount() {
        return jobThreadCount;
    }

    public void setJobThreadCount(int jobThreadCount) {
        this.jobThreadCount = jobThreadCount;
    }

    public String isMakeSchedulerThreadDaemon() {
        return makeSchedulerThreadDaemon;
    }

    public void setMakeSchedulerThreadDaemon(String makeSchedulerThreadDaemon) {
        this.makeSchedulerThreadDaemon = makeSchedulerThreadDaemon;
    }

    public boolean isEnableScanReminderJob() {
        return enableScanReminderJob;
    }

    public void setEnableScanReminderJob(boolean enableScanReminderJob) {
        this.enableScanReminderJob = enableScanReminderJob;
    }

    public boolean isDisableScheduler() {
        return disableScheduler;
    }

    public void setDisableScheduler(boolean disableScheduler) {
        this.disableScheduler = disableScheduler;
    }

    public String getClusterInstanceNames() {
        return clusterInstanceNames;
    }

    public void setClusterInstanceNames(String clusterInstanceNames) {
        this.clusterInstanceNames = clusterInstanceNames;
    }

    public boolean isDisabledUserAssigneeProvider() {
        return disabledUserAssigneeProvider;
    }

    public void setDisabledUserAssigneeProvider(boolean disabledUserAssigneeProvider) {
        this.disabledUserAssigneeProvider = disabledUserAssigneeProvider;
    }

    public boolean isDisabledDeptAssigneeProvider() {
        return disabledDeptAssigneeProvider;
    }

    public void setDisabledDeptAssigneeProvider(boolean disabledDeptAssigneeProvider) {
        this.disabledDeptAssigneeProvider = disabledDeptAssigneeProvider;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isDisableDefaultTaskDiagramInfoProvider() {
        return disableDefaultTaskDiagramInfoProvider;
    }

    public void setDisableDefaultTaskDiagramInfoProvider(boolean disableDefaultTaskDiagramInfoProvider) {
        this.disableDefaultTaskDiagramInfoProvider = disableDefaultTaskDiagramInfoProvider;
    }

    public String getDefaultFileStoreDir() {
        return defaultFileStoreDir;
    }

    public void setDefaultFileStoreDir(String defaultFileStoreDir) {
        this.defaultFileStoreDir = defaultFileStoreDir;
    }

    public boolean isDisableDefaultFileProcessProvider() {
        return disableDefaultFileProcessProvider;
    }

    public void setDisableDefaultFileProcessProvider(boolean disableDefaultFileProcessProvider) {
        this.disableDefaultFileProcessProvider = disableDefaultFileProcessProvider;
    }

    public boolean getDiagramShowTime() {
        return diagramShowTime;
    }

    public void setDiagramShowTime(boolean diagramShowTime) {
        this.diagramShowTime = diagramShowTime;
    }

    public String getDiagramPassedNodeBgcolor() {
        return diagramPassedNodeBgcolor;
    }

    public void setDiagramPassedNodeBgcolor(String diagramPassedNodeBgcolor) {
        this.diagramPassedNodeBgcolor = diagramPassedNodeBgcolor;
    }

    public String getDiagramPassedNodeFontColor() {
        return diagramPassedNodeFontColor;
    }

    public void setDiagramPassedNodeFontColor(String diagramPassedNodeFontColor) {
        this.diagramPassedNodeFontColor = diagramPassedNodeFontColor;
    }

    public int getDiagramPassedNodeFontSize() {
        return diagramPassedNodeFontSize;
    }

    public void setDiagramPassedNodeFontSize(int diagramPassedNodeFontSize) {
        this.diagramPassedNodeFontSize = diagramPassedNodeFontSize;
    }

    public String getDiagramPassedNodeBorderColor() {
        return diagramPassedNodeBorderColor;
    }

    public void setDiagramPassedNodeBorderColor(String diagramPassedNodeBorderColor) {
        this.diagramPassedNodeBorderColor = diagramPassedNodeBorderColor;
    }

    public String getDiagramPassedConnectionColor() {
        return diagramPassedConnectionColor;
    }

    public void setDiagramPassedConnectionColor(String diagramPassedConnectionColor) {
        this.diagramPassedConnectionColor = diagramPassedConnectionColor;
    }

    public String getDiagramPassedConnectionFontColor() {
        return diagramPassedConnectionFontColor;
    }

    public void setDiagramPassedConnectionFontColor(String diagramPassedConnectionFontColor) {
        this.diagramPassedConnectionFontColor = diagramPassedConnectionFontColor;
    }

    public int getDiagramPassedConnectionFontSize() {
        return diagramPassedConnectionFontSize;
    }

    public void setDiagramPassedConnectionFontSize(int diagramPassedConnectionFontSize) {
        this.diagramPassedConnectionFontSize = diagramPassedConnectionFontSize;
    }

    public String getDiagramMultiCurrentNodeBgcolor() {
        return diagramMultiCurrentNodeBgcolor;
    }

    public void setDiagramMultiCurrentNodeBgcolor(String diagramMultiCurrentNodeBgcolor) {
        this.diagramMultiCurrentNodeBgcolor = diagramMultiCurrentNodeBgcolor;
    }

    public String getDiagramMultiCurrentNodeFontColor() {
        return diagramMultiCurrentNodeFontColor;
    }

    public void setDiagramMultiCurrentNodeFontColor(String diagramMultiCurrentNodeFontColor) {
        this.diagramMultiCurrentNodeFontColor = diagramMultiCurrentNodeFontColor;
    }

    public int getDiagramMultiCurrentNodeFontSize() {
        return diagramMultiCurrentNodeFontSize;
    }

    public void setDiagramMultiCurrentNodeFontSize(int diagramMultiCurrentNodeFontSize) {
        this.diagramMultiCurrentNodeFontSize = diagramMultiCurrentNodeFontSize;
    }

    public String getDiagramMultiCurrentNodeBorderColor() {
        return diagramMultiCurrentNodeBorderColor;
    }

    public void setDiagramMultiCurrentNodeBorderColor(String diagramMultiCurrentNodeBorderColor) {
        this.diagramMultiCurrentNodeBorderColor = diagramMultiCurrentNodeBorderColor;
    }

    public String getDiagramConnectionColor() {
        return diagramConnectionColor;
    }

    public void setDiagramConnectionColor(String diagramConnectionColor) {
        this.diagramConnectionColor = diagramConnectionColor;
    }

    public String getDiagramConnectionFontColor() {
        return diagramConnectionFontColor;
    }

    public void setDiagramConnectionFontColor(String diagramConnectionFontColor) {
        this.diagramConnectionFontColor = diagramConnectionFontColor;
    }

    public int getDiagramConnectionFontSize() {
        return diagramConnectionFontSize;
    }

    public void setDiagramConnectionFontSize(int diagramConnectionFontSize) {
        this.diagramConnectionFontSize = diagramConnectionFontSize;
    }

    public String getDiagramNodeBgcolor() {
        return diagramNodeBgcolor;
    }

    public void setDiagramNodeBgcolor(String diagramNodeBgcolor) {
        this.diagramNodeBgcolor = diagramNodeBgcolor;
    }

    public String getDiagramNodeFontColor() {
        return diagramNodeFontColor;
    }

    public void setDiagramNodeFontColor(String diagramNodeFontColor) {
        this.diagramNodeFontColor = diagramNodeFontColor;
    }

    public int getDiagramNodeFontSize() {
        return diagramNodeFontSize;
    }

    public void setDiagramNodeFontSize(int diagramNodeFontSize) {
        this.diagramNodeFontSize = diagramNodeFontSize;
    }

    public String getDiagramNodeBorderColor() {
        return diagramNodeBorderColor;
    }

    public void setDiagramNodeBorderColor(String diagramNodeBorderColor) {
        this.diagramNodeBorderColor = diagramNodeBorderColor;
    }

    public String getDiagramCurrentNodeBgcolor() {
        return diagramCurrentNodeBgcolor;
    }

    public void setDiagramCurrentNodeBgcolor(String diagramCurrentNodeBgcolor) {
        this.diagramCurrentNodeBgcolor = diagramCurrentNodeBgcolor;
    }

    public String getDiagramCurrentNodeFontColor() {
        return diagramCurrentNodeFontColor;
    }

    public void setDiagramCurrentNodeFontColor(String diagramCurrentNodeFontColor) {
        this.diagramCurrentNodeFontColor = diagramCurrentNodeFontColor;
    }

    public int getDiagramCurrentNodeFontSize() {
        return diagramCurrentNodeFontSize;
    }

    public void setDiagramCurrentNodeFontSize(int diagramCurrentNodeFontSize) {
        this.diagramCurrentNodeFontSize = diagramCurrentNodeFontSize;
    }

    public String getDiagramCurrentNodeBorderColor() {
        return diagramCurrentNodeBorderColor;
    }

    public void setDiagramCurrentNodeBorderColor(String diagramCurrentNodeBorderColor) {
        this.diagramCurrentNodeBorderColor = diagramCurrentNodeBorderColor;
    }
}