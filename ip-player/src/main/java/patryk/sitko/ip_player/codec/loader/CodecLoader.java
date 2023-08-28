package patryk.sitko.ip_player.codec.loader;

import java.awt.Canvas;
import java.awt.Color;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.x.LibXUtil;

public class CodecLoader extends Canvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static MediaPlayerFactory mediaPlayerFactory;
	private EmbeddedMediaPlayer embeddedMediaPlayer;
	private static Win32FullScreenStrategy strategy;

	public CodecLoader(Win32FullScreenStrategy strategy) {
		CodecLoader.strategy = strategy;
		this.setupVideoPlayer();
	}

	private void setupVideoPlayer() {
		setBackground(Color.black);
		embeddedMediaPlayer = getPlayer();
		embeddedMediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(this));
	}

	public EmbeddedMediaPlayer getPlayer() {
		String lib = RuntimeUtil.getLibVlcLibraryName();
		NativeLibrary.addSearchPath(lib, "lib/Codecs");
		Native.loadLibrary(lib, LibVlc.class);
		LibXUtil.initialise();
		mediaPlayerFactory = new MediaPlayerFactory();
		return mediaPlayerFactory.newEmbeddedMediaPlayer(strategy);
	}

	public void startPlaying(String URL) {
		embeddedMediaPlayer.playMedia(URL);
	}

	public void stopPlaying() {
		embeddedMediaPlayer.stop();
	}

	public Boolean isPlaying() {
		return embeddedMediaPlayer.isPlaying();
	}

}
