package xyz.klenkiven.auth.config;

import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedisSessionFlashMapManager extends SessionFlashMapManager {

    private static final String FLASH_MAPS_SESSION_ATTRIBUTE = SessionFlashMapManager.class.getName() + ".FLASH_MAPS";

    @Override
    protected List<FlashMap> retrieveFlashMaps(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : renderFlashMaps(session);
    }

    @SuppressWarnings("unchecked")
    private List<FlashMap> renderFlashMaps(HttpSession session) {

        List<HashMap<String, Object>> maps = (List<HashMap<String, Object>>)session.getAttribute(FLASH_MAPS_SESSION_ATTRIBUTE);
        if (CollectionUtils.isEmpty(maps)){
            return null;
        }

        List<FlashMap> flashMaps = new ArrayList<>(maps.size());
        FlashMap flashMap;
        for (Map<String, Object> map : maps){
            flashMap = new FlashMap();
            flashMap.putAll(map);
            flashMaps.add(flashMap);
        }

        return flashMaps;
    }
}
