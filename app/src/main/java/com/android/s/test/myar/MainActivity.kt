package com.android.s.test.myar

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MotionEvent
import android.widget.Toast
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import java.util.*


class MainActivity : AppCompatActivity() {
    private var arFragment:ArFragment? = null
    private var treeRenderable: ModelRenderable? = null
    private var textRenderable: ViewRenderable? = null
    private var redSphereRenderable:ModelRenderable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment?


        ViewRenderable.builder()
            .setView(this, R.layout.ar_text_view)
            .build()
            .thenAccept {
                renderable -> textRenderable = renderable
            }


        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        ModelRenderable.builder()
            .setSource(this, R.raw.tree)
            .build()
            .thenAccept { renderable: ModelRenderable ->
                treeRenderable = renderable
            }
            .exceptionally { throwable: Throwable? ->
                val toast =
                    Toast.makeText(this, "Unable to load tree renderable", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                null
            }


        MaterialFactory.makeOpaqueWithColor(this, Color(android.graphics.Color.RED))
            .thenAccept { material: Material? ->
                redSphereRenderable =
                    ShapeFactory.makeSphere(0.1f, Vector3(0.0f, 0.15f, 0.0f), material)
            }


        val node = Node()
        node.setParent(arFragment!!.arSceneView.scene)
        node.renderable = redSphereRenderable


        arFragment!!.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane?, motionEvent: MotionEvent? ->
            if (treeRenderable == null) {
                return@setOnTapArPlaneListener
            }

            // Create the Anchor.
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment!!.arSceneView.scene)

//            textRenderable?.let {
//                Node().apply {
//                    setParent(anchorNode)
//                    localPosition = Vector3(-0.3f, 0.3f, 0.3f)
//                    renderable = it
//                }
//            }
//
//            redSphereRenderable?.let {
//                Node().apply {
//                    setParent(anchorNode)
//                    localPosition = Vector3(0.3f, -0.3f, -0.3f)
//                    renderable = it
//                }
//            }
//
//            treeRenderable?.let {
//                Node().apply {
//                    setParent(anchorNode)
//                    renderable = it
//                }
//            }

//            textRenderable?.let {
//                TransformableNode(arFragment!!.transformationSystem).apply {
//                    setParent(anchorNode)
//                    localPosition = Vector3(-0.3f, 0.3f, 0.3f)
//                    renderable = it
//                    select()
//                }
//            }
//
//            redSphereRenderable?.let {
//                TransformableNode(arFragment!!.transformationSystem).apply {
//                    setParent(anchorNode)
//                    localPosition = Vector3(0.3f, -0.3f, -0.3f)
//                    renderable = it
//                    select()
//                }
//            }

            treeRenderable?.let {
                TransformableNode(arFragment!!.transformationSystem).apply {
                    setParent(anchorNode)
                    renderable = it
                    select()
                }
            }


                // Create the transformable andy and add it to the anchor.
//            val tree =
//                TransformableNode(arFragment!!.transformationSystem)
//            tree.setParent(anchorNode)
//            tree.renderable = treeRenderable
//            tree.select()
        }
    }
}