package util;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class MeshFactory {
    public static MeshView buildRegularPyramid(int sides, float negativeHeight, float radius, PhongMaterial material){
        float height = -negativeHeight;
        TriangleMesh mesh = new TriangleMesh();
        double angle = Math.toRadians(360/sides);
        mesh.getPoints().addAll(0, 0, 0);//point 0
        for (int i = 0; i < sides; i++) {//generate the vertices around the shape
            float x = (float)Math.cos(angle*i)*radius;
            float z = (float)Math.sin(angle*i)*radius;
            float y = (float)0;
            mesh.getPoints().addAll(x,y,z);
        }
        mesh.getPoints().addAll(0, height,0);

        mesh.getTexCoords().addAll(
                0.0f, 0.0f,
                0.5f, 1.0f,
                1.0f, 0.0f
        );
        for (int i = 1; i < sides; i++) {//generate the faces based on the vertices
            mesh.getFaces().addAll(i,0,i+1,2,sides+1,1);
        }
        mesh.getFaces().addAll(sides,0, 1, 2, sides+1,1);

        MeshView mv = new MeshView(mesh);
        mv.setMaterial(material);
        return mv;
    }

}
