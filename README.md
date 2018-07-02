# 1. Trích xuất mfcc từ file. 

          Danh sách tham số:
          .audioFile file đã ghi âm.
          .audioBufferSize độ lớn của mỗi frame (trích xuất mfcc).
          .bufferOverlap số tín hiệu chồng lên nhau(frameshift).
          
          dispatcher = AudioDispatcherFactory.fromFile(new File("1530074385995.wav"), 1024, 0);
          dispatcher.addAudioProcessor(new AudioProcessor() {
          @Override
          public void processingFinished() {
            System.out.println("Size : " + size);
            System.out.println("Count : " + mfccs.size());
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < mfccs.size(); i++) {
              float[] mfcc = mfccs.get(i);
              StringBuilder data = new StringBuilder();
              for (double cell : mfcc) {
                data.append(", ").append(cell);
              }
              builder.append(data.substring(2, data.length() - 2)).append("\r\n");
            }

            new MFCCs().saveFile(builder.toString());
          }

          @Override
          public boolean process(AudioEvent audioEvent) {
            size += audioEvent.getBufferSize();
            MFCC mfcc = new MFCC(1024, 44100, 39, 40, 300, 8000);
            mfcc.process(audioEvent);

            boolean isSuccess = mfcc.process(audioEvent);
            if (isSuccess) {
              mfccs.add(mfcc.getMFCC());
            }

            printArray("mfccs", mfcc.getMFCC());
            return true;
          }
          });
          dispatcher.run();

# 2. Trích xuất mfcc.

          .audioBufferSize: buffersize của đoạn ghi âm setup trong recorder
          .sampleRate: sample rate của đoạn ghi âm setup trong recorder
          .amountOfCepstrumCoef: Số đặc trưng mfcc muốn lấy.
          .amountOfMelFilters: Số băng lọc mel
          .lowerFilterFreq: Tần số nhỏ nhất giọng nói của người(thường là 300)
          .upperFilterFreq: Tần số lớn nhất giọng nói của người(thường là 8000)

          MFCC mfcc = new MFCC(1024, 44100, 39, 40, 300, 8000);
          mfcc.process(audioEvent);
