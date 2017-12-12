package wsd17z.togetter;

import jadex.android.service.JadexPlatformService;
import jadex.base.PlatformConfiguration;
import jadex.base.RootComponentConfiguration;

/**
 * Created by Kosmos on 10/12/2017.
 */

public class MyJadexService extends JadexPlatformService {
    public static final String PLATFORM_NAME = "TogetterPlatform";
    public MyJadexService() {
        super();
        setPlatformAutostart(false);
        PlatformConfiguration config = getPlatformConfiguration();
        config.setPlatformName(PLATFORM_NAME);
        config.addComponent("wsd17z.togetter.Agents.ClientManagementAgent.class");
        config.addComponent("wsd17z.togetter.Agents.DbManagementAgent.class");
        RootComponentConfiguration rootConfig = config.getRootConfig();
        rootConfig.setAwareness(true);
        rootConfig.setKernels(KERNEL_MICRO);
    }
}