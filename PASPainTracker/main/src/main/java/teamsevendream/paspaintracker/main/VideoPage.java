package teamsevendream.paspaintracker.main;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.net.Uri;
import android.widget.VideoView;
import android.widget.MediaController;

public class VideoPage extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;
    private String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_page);
        videoView = findViewById(R.id.videoView);
        mediaController = new MediaController(this);
        videoPath = getIntent().getStringExtra("videoPath");
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.start();
    }

}
