package com.tza.phantasia.Entities;

import com.tza.phantasia.MapParser.World;
import com.tza.phantasia.Renderer.VisibleEntity;
import java.util.ArrayList;
import java.util.List;

import static com.tza.phantasia.Main.getRenderer;
import static com.tza.phantasia.Main.MINIMAP_SCALE;

public class MiniMap {
    public class MiniMapItem {
        private final VisibleEntity visibleEntity = getRenderer().addVisibleEntity().
                setCamerable(false).
                setScale(MINIMAP_SCALE);

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

        @Override
        protected void finalize() throws Throwable {
            visibleEntity.remove();
            super.finalize();
        }
    }

    public VisibleEntity visibleEntity = getRenderer().addVisibleEntity().setScale(MINIMAP_SCALE);
    private List<MiniMapItem> items = new ArrayList<>();
    int mapWidth, mapHeight, miniWidth, miniHeight;

    public MiniMap(World map) {
        mapWidth = map.getVisibleEntity().getRenderable().getWidth();
        mapHeight = map.getVisibleEntity().getRenderable().getHeight();
        miniWidth = (int)(mapWidth * MINIMAP_SCALE);
        miniHeight = (int)(mapHeight * MINIMAP_SCALE);
    }

    public MiniMapItem addItem(String resource) {
        return new MiniMapItem(resource);
    }

    public void destroy() {
        items.forEach(MiniMapItem::remove);
        visibleEntity.remove();
    }

}
