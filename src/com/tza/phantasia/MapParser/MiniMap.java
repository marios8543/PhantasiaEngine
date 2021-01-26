package com.tza.phantasia.MapParser;

import com.tza.phantasia.MapParser.World;
import com.tza.phantasia.Renderer.VisibleEntity;
import java.util.ArrayList;
import java.util.List;

import static com.tza.phantasia.Phantasia.getRenderer;

public class MiniMap {
    public class MiniMapItem {
        private final VisibleEntity visibleEntity = getRenderer().addVisibleEntity().
                setCamerable(false).
                setScale(scale);

        private MiniMapItem(String resource) {
            visibleEntity.setResourceName(resource);
        }

        public void setX(int x) {
            visibleEntity.setX_pos((miniWidth * x)/mapWidth);
        }

        public void setY(int y) {
            visibleEntity.setY_pos((miniHeight * y)/mapHeight);
        }

        public void remove() {
            visibleEntity.remove();
        }
    }

    public final VisibleEntity visibleEntity = getRenderer().addVisibleEntity();
    private final List<MiniMapItem> items = new ArrayList<>();
    final int mapWidth;
    final int mapHeight;
    final int miniWidth;
    final int miniHeight;
    final int scale;

    public MiniMap(World map, int scale1) {
        scale = scale1;
        mapWidth = map.getVisibleEntity().getRenderable().getWidth();
        mapHeight = map.getVisibleEntity().getRenderable().getHeight();
        miniWidth = mapWidth * scale;
        miniHeight = mapHeight * scale;
        visibleEntity.setScale(scale);
    }

    public MiniMapItem addItem(String resource) {
        return new MiniMapItem(resource);
    }

    public void destroy() {
        items.forEach(MiniMapItem::remove);
        visibleEntity.remove();
    }

}
