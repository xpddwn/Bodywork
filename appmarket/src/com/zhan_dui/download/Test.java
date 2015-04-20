package com.zhan_dui.download;

import java.io.IOException;

public class Test {

	public static void main(String[] args) {
		DownloadManager downloadManager = DownloadManager.getInstance();
		String qQString = "http://a.hiphotos.baidu.com/image/pic/item/63d0f703918fa0ecb0d225aa259759ee3d6ddb04.jpg";
		String APK = "http://d.hiphotos.baidu.com/image/pic/item/72f082025aafa40f30528fdca964034f78f0191e.jpg";
		String phaseString = "http://g.hiphotos.baidu.com/image/pic/item/a1ec08fa513d2697c77eace756fbb2fb4216d8df.jpg";
	

		/*** type you save direcotry ****/
		String saveDirectory = "C:\\Users\\tuomao\\Desktop\\downfile";
		try {
			DownloadMission mission = new DownloadMission(qQString,
					saveDirectory, "test1.jpg");
			downloadManager.addMission(mission);
			DownloadMission mission2 = new DownloadMission(APK,
					saveDirectory, "test2.jpg");
			downloadManager.addMission(mission2);
			DownloadMission mission3 = new DownloadMission(phaseString, saveDirectory,
					"test3.jpg");
			downloadManager.addMission(mission3);
			downloadManager.start();
			
			//int counter = 0;
			/*while (true) {
				// System.out.println("The mission has finished :"
				// + mission.getReadableSize() + "Active Count:"
				// + mission.getActiveTheadCount() + " speed:"
				// + mission.getReadableSpeed() + " status:"
				// + mission.isFinished() + " AverageSpeed:"
				// + mission.getReadableAverageSpeed() + " MaxSpeed:"
				// + mission.getReadableMaxSpeed() + " Time:"
				// + mission.getTimePassed() + "s");
				System.out.println("Downloader information Speed:"
						+ downloadManager.getReadableTotalSpeed()
						+ " Down Size:"
						+ downloadManager.getReadableDownloadSize());
				Thread.sleep(1000);
				counter++;
				// if (counter == 6) {
				// mission.pause();
				// }
				// if (counter == 11) {
				// downloadManager.start();
				// }
			}*/
		} catch (IOException e1) {
			e1.printStackTrace();
		} /*catch (InterruptedException e) {
			e.printStackTrace();
		}*/
	}
}
