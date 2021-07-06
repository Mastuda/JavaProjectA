import java.applet.*;		//wavファイルの再生に使用
import java.awt.*; 			//event.KeyEvent;
import java.awt.event.*; 	//KeyListener;
import javax.swing.*; 		//JFrame;
import static java.lang.Math.PI;
import java.util.Random;

public class actionP extends JFrame {
	final int windowWidth = 800;
	final int windowHeight = 600;
	public static void main(String[] args) {
		new actionP();
	}
	
	Dimension dimOfPanel;
	ImageIcon iconMe, iconEnemy;
	Image[] imgMe = new Image[4];
	Image[] imgEnemy = new Image[4];
	Image[] imgEnmatk = new Image[4];
	Image imgatkUP, imghpUP, imgspdUP, imgdefUP;
	int muki = 2; //上下左右、0123
	boolean isMove;
	boolean atk = false;
	int Rate = 0;
	boolean kaminari = false;
	int danX, danY;
	int Rmove = 0, skilland = -1;
	int icount = 0;
	int tmp = 0, tmp2;
	int iconrand;
	int itemX, itemY, icontmp;
	String buffsound, eisyou="パワーチャージ.wav";

	/* 自分の変数 */
	int myHP = 100;
	int myHeight, myWidth;
	int myX, myY;
	int kisomyatk = 10, danmyatk = 20;
	int myspd = 25, mydef = 0;
	Image effect;
	int effW, effH;
	int acount = 0, dantmp = 0;
	boolean dan = false, isdanActive = false, iskey = false;
	Image imgdan;
	int skilldelay = -1;
	int Mydelay = -1;
	boolean delay = false;
	int buffmyatk = 0, buffmyspd = 0, buffmydef = 0;
	String dansound = "se03.wav";
	
	/* 相手の変数 */
	int enemyHP = 200;
	int enemyWidth, enemyHeight;
	int enemyX = 0, enemyY = 0;
	int Emuki;
	int kisoEatk = 20;
	int enespd = 25, enedef = 0;
	int kmnrX, kmnrY, maenX, maenY, makuX, makuY, enetmp = 0, kmnrtmp = 0, entmp = 0;
	Image imgkaminari, imgmahoen;
	boolean enmatk = false;
	boolean makuon = false;
	boolean enemyUP = false;
	int buffeneatk = 0, buffenespd = 0, buffenedef = 0;
	int Edelay = -1;
	String enmatksound ="重いパンチ2.wav";
	String kmnrsound = "雷魔法1.wav";
	String mahoensound = "重力魔法2.wav";
	String makusound = "ビームガン.wav";
	String EXsound = "警報が鳴る.wav";

	public actionP() {
		Dimension dimOfScreen = Toolkit.getDefaultToolkit().getScreenSize();

		setBounds(dimOfScreen.width/2 - windowWidth/2
				, dimOfScreen.height/2 - windowHeight/2
				, windowWidth, windowHeight);//ウインドウの位置と初期サイズ
		setResizable(false);
		setTitle("試練の遺跡の調査");//タイトル
		setDefaultCloseOperation(EXIT_ON_CLOSE);//閉じ方

		MyJPanel panel= new MyJPanel();
		Container c = getContentPane();
			c.add(panel);
		setVisible(true);
	}

	public class MyJPanel extends JPanel implements
		 KeyListener, ActionListener, MouseListener, MouseMotionListener{
		//タイマー
		Timer timer;
		//コンストラクタ(開始時の初期化)
		public MyJPanel() {
			setBackground(Color.black);
			addMouseListener(this);
			addMouseMotionListener(this);
			isMove = false;
			addKeyListener(this);
			timer = new Timer(50, this);//ここの数字ミリ秒ごとに動く
			timer.start();
			
			setFocusable(true);
			imgMe[0] = getImg("ME.png");
			imgMe[1] = getImg("MEm.png");
			imgMe[2] = getImg("MEu.png");
			imgMe[3] = getImg("MEh.png");
			imgEnemy[0] = getImg("BOSS.png");
			imgEnemy[1] = getImg("BOSSm.png");
			imgEnemy[2] = getImg("BOSSu.png");
			imgEnemy[3] = getImg("BOSSh.png");
			imgEnmatk[0] = getImg("BOSS_atk.png");
			imgEnmatk[1] = getImg("BOSSm_atk.png");
			imgEnmatk[2] = getImg("BOSSu_atk.png");
			imgEnmatk[3] = getImg("BOSSh_atk.png");
			enemyWidth = imgEnemy[0].getWidth(this);
			enemyHeight = imgEnemy[0].getHeight(this);
			effect = getImg("effect.png");
			imgkaminari = getImg("thunder.png");
			imgmahoen =getImg("mahoen.png");
			imgdan = getImg("dan.png");
			imgatkUP = getImg("icon_atkup.png");
			imghpUP = getImg("icon_hpup.png");
			imgspdUP = getImg("icon_spdup.png");
			imgdefUP = getImg("icon_defup.png");
			
			initMe();
			initEnemy();

		}
		/* パネル上の描画 */
		public void paintComponent(Graphics g) {
			dimOfPanel = getSize();
			super.paintComponent(g);

			drawMe(g);		//自分
			drawEnemy(g);	//相手
			
			//相手移動処理
			Random random = new Random();
			int do1 = random.nextInt(100);
			int do2 = random.nextInt(100);
			
			if(enemyY-myY <= 75 && myY-enemyY <= 75) { //相手との距離
				if(enemyX-myX <= 75 && myX-enemyX <= 75) {
					enmatk = true;
				}
			}
			if(skilland==0) moveEnemy(g);	// 相手の移動

			if(Edelay == -1) {
				if(enmatk == true) {//相手の通常攻撃
					g.drawImage(imgEnmatk[Rmove], enemyX, enemyY, enemyX+150, enemyY+150, 200,0,400,200,this);
					if(enetmp==0) sound(enmatksound);
					enetmp++;
					if(enetmp==5) { 
						enetmp = 0;
						if(Rmove==0) enemyY -= 25;
						else if(Rmove==1) enemyX -= 25;
						else if(Rmove==2) enemyY += 25;
						else if(Rmove==3) enemyX += 25;
						acount = 0;
						enmatk = false;
						Edelay++;
						if(enemyY-myY <= 100 && myY-enemyY <= 100 && enemyX-myX <= 100 && myX-enemyX <= 100) {
							//当たっていたら
							myHP -= (kisoEatk + buffeneatk) - mydef;
						}
					}
				}//enmatkかっこの終わり
				if(skilland==1 && icount%10 == 0) { //相手スキル1・雷
					if(kmnrtmp == 0) {/*待機時間*/
						kmnrX = myX-40;
						kmnrY = myY-100;
						sound(eisyou);
					}
					kmnrtmp++;
					if(kmnrtmp==2 && icount%20 == 0) {
						sound(kmnrsound);
						g.drawImage(imgkaminari, kmnrX, kmnrY,this);
						if(kmnrY-myY <= 50 && myY-kmnrY <= 50 && kmnrX-myX <= 50 && myX-kmnrX <= 50) {
							myHP -= (kisoEatk + buffeneatk) - buffmydef;
						}
					}
					if(kmnrtmp == 2) kmnrtmp = 0;
				}//雷かっこの終わり
				
				if(skilland==2 && icount%10 == 0) { //相手スキル2・円形
					if(entmp == 0) {/*待機時間*/
						sound(eisyou);
					}
					maenX = enemyX-50;
					maenY = enemyY;
					entmp++;
					if(entmp==1) sound(mahoensound);
					if(entmp>=1) {
						g.drawImage(imgmahoen, maenX, maenY, this);
						if(maenY-myY <= 50 && myY-maenY <= 50 && maenX-myX <= 50 && myX-maenX <= 50) {
							myHP -= (kisoEatk + buffeneatk) - buffmydef;
						}
					}
					if(entmp == 4) entmp = 0;
				}//円かっこの終わり
			}else {//delayがある時
				Edelay++;
				if(Edelay==30) {
					Edelay = -1; //delayリセット
				}
			}
			
			if(Mydelay == -1) {
				if(atk==true) {//自分の通常攻撃
					iskey = true;
					String myatk = "sword1.wav";
					sound(myatk);
					Graphics2D g2d = (Graphics2D)g;
					Dimension size = getSize();
					switch(muki) {
					case 0://下
						Rate = 180; effW = myX+100; effH = myY+150;
						break;
					case 1://右
						Rate = 90; effW = myX+150; effH = myY;
						break;
					case 2://上
						Rate = 0; effW = myX; effH = myY-50;
						break;
					case 3://左
						Rate = 270; effW = myX-50; effH = myY+100;
						break;
					}
					g2d.rotate(Rate*PI/180, effW, effH);
					g.drawImage(effect, effW, effH, this);
					g2d.rotate(-Rate*PI/180, effW, effH);
					if(enemyY-myY <= 135 && myY-enemyY <= 135 && enemyX-myX <= 135 && myX-enemyX <= 135) {
						enemyHP -= (kisomyatk + buffmyatk) - buffenedef;
						iskey = false;
					}
					atk = false;
					Mydelay++;
					acount++;
				}
			}else {
				Mydelay++;
				if(acount!=3 && Mydelay>=9) {
					Mydelay = -1;
					iskey = false;
				}
				if(acount==3 && Mydelay>=30) {
					Mydelay = -1;
					acount = 0;
					iskey = false;
				}
			}//atkかっこの終わり
			if(dan==true) {//自分スキル・弾
				if(dantmp == 0) {
					tmp2 = muki;
					Graphics2D g2d = (Graphics2D)g;
					Dimension size = getSize();
					switch(tmp2) {
					case 0://下
						Rate = 180; danX = myX + 75; danY = myY+150;
						break;
					case 1://右
						Rate = 90; danX = myX+150; danY = myY+50;
						break;
					case 2://上
						Rate = 0; danX = myX+25; danY = myY-50;
						break;
					case 3://左
						Rate = 270; danX = myX-100; danY = myY+100;
						break;
					}
					g2d.rotate(Rate*PI/180, danX, danY);
					g2d.drawImage(imgdan, danX, danY,this);
					sound(dansound);
					skilldelay++;
					g2d.rotate(-Rate*PI/180, danX, danY);
				}else {
					Graphics2D g2d = (Graphics2D)g;
					Dimension size = getSize();
					switch(tmp2) {
					case 0://下
						Rate = 180; danY += 50;
						break;
					case 1://右
						Rate = 90; danX += 50;
						break;
					case 2://上
						Rate = 0; danY -= 50;
						break;
					case 3://左
						Rate = 270; danX -= 50;
						break;
					}
					g2d.rotate(Rate*PI/180, danX, danY);
					g2d.drawImage(imgdan, danX, danY,this);
					g2d.rotate(-Rate*PI/180, danX, danY);
				}
				
				dantmp++;
				if(danX-enemyX <= 150 && enemyX-danX <= 150 && danY-enemyY <= 150 && enemyY-danY <= 150) {
					enemyHP -= (danmyatk + buffmyatk) - enedef;
					dan = false;
					dantmp = 0;
				}
				if(danX < 0 || danY < 0 || danX > windowWidth || danY > windowHeight) {
					dan = false;
					dantmp = 0;
				}
			}//弾かっこ終わり
			if(skilldelay>=0) {
				skilldelay++;
				if(skilldelay == 60) {
					skilldelay = -1;
				}
			}

			if(iconrand >= 1) {//補助アイテム
				if(icontmp == 0) {
					Random item = new Random();
					itemX = item.nextInt(windowWidth - 100);
					itemY = item.nextInt(windowHeight - 100);
					icontmp = 1;
				}
				if(iconrand==1) g.drawImage(imgatkUP, itemX, itemY,this);
				if(iconrand==2) g.drawImage(imghpUP, itemX, itemY,this);
				if(iconrand==3) g.drawImage(imgspdUP, itemX, itemY,this);
				if(iconrand==4) g.drawImage(imgdefUP, itemX, itemY,this);
				if(itemX-myX <= 50 && myX-itemX <= 50 && itemY-myY <= 50 && myY-itemY <= 50) {
					buffmyatk = 0; buffmyspd = 0; buffmydef = 0;
					buffeneatk = 0; buffenespd = 0; buffenedef = 0;
					if(iconrand==1) {
						buffsound = "パワーアップ.wav";
						sound(buffsound);
						buffmyatk += 20;
					}
					if(iconrand==2) {
						buffsound = "ステータス治療1.wav";
						sound(buffsound);
						myHP += 20;
					}
					if(iconrand==3) {
						buffsound = "スピードアップ.wav";
						sound(buffsound);
						buffmyspd += 30;
					}
					if(iconrand==4) {
						buffsound = "魔法反射.wav";
						sound(buffsound);
						buffmydef += 10;
					}
					icontmp = 0;
				}
				if(itemX-enemyX <= 150 && enemyX-itemX <= 150 && itemY-enemyY <= 150 && enemyY-itemY <= 150) {
					if(iconrand==1) {
						buffsound = "パワーアップ.wav";
						sound(buffsound);
						buffeneatk += 20;
					}
					if(iconrand==2) {
						buffsound = "ステータス治療1.wav";
						sound(buffsound);
						enemyHP += 20;
					}
					if(iconrand==3) {
						buffsound = "スピードアップ.wav";
						sound(buffsound);
						buffenespd += 25;
					}
					if(iconrand==4) {
						buffsound = "魔法反射.wav";
						sound(buffsound);
						buffenedef += 10;
					}
					icontmp = 0;
				}
			}
			if(enemyHP <= 100 && enemyUP == false) {
				sound(EXsound);
				kisoEatk *= 3/2;
				enemyUP = true;
			}
			if(enemyHP <= 0) {
				enemyHP = 0;
				Image WIN = getImg("WIN.png");
				g.drawImage(WIN, 200, 150, this);
				timer.stop();
			}
			if(myHP <= 0) {
				myHP = 0;
				Image LOSE = getImg("LOSE.png");
				g.drawImage(LOSE, 100, 150, this);
				timer.stop();
			}
			/*数値表示*/
			printHP(g);

		}
		public void printHP(Graphics g) {
			Font font = new Font("SansSerif", Font.BOLD, 24);
			g.setFont(font);
			g.setColor(Color.YELLOW);
			g.drawString("me HP"+myHP, 4, 25);
			g.drawString("BOSS HP"+enemyHP, windowWidth-170, 25);
			
		}
		//サウンド
		public void sound(String soundname) {
			AudioClip oto;
			oto = Applet.newAudioClip(getClass().getResource(soundname));
			oto.play();
		}
		
		public void initMe() {//位置の初期化
			myX = windowWidth/2 - 200/2;
			myY = windowHeight/2;
		}
		public void initEnemy() {//位置の初期化
			enemyX = windowWidth/2 - 200/2;
			enemyY = 0;
		}

		/* 画像ファイルから Image クラスへの変換 */
		public Image getImg(String filename) {
			ImageIcon icon = new ImageIcon(filename);
			Image img = icon.getImage();
			return img;
		}

		/*自分の描画*/
		public void drawMe(Graphics g) {
			g.drawImage(imgMe[muki], myX, myY, myX+100, myY+100, 0,0,200,200,this);
		}
		/*相手の描画*/
		public void drawEnemy(Graphics g) {
			if(enmatk==false) g.drawImage(imgEnemy[Rmove], enemyX, enemyY, enemyX+100, enemyY+100, 0,0,200,200,this);
			if(enmatk==true) g.drawImage(imgEnmatk[Rmove], enemyX, enemyY, enemyX+100, enemyY+100, 0,0,200,200,this);
		}
		
		public void moveEnemy(Graphics g) {
			if(icount%2 == 0 && enmatk == false) {//タイマー%2かつatkなし
				if(enemyY < myY && (50 > enemyY-myY || enemyY-myY > -50)) {
					Rmove = 0;
				}
				if(enemyY > myY) {
					Rmove = 2;
				}
				if(enemyX < myX) {
					Rmove = 1;
				}
				if(enemyX > myX) {
					Rmove = 3;
				}
				switch (Rmove) {
				case 2://上
					if(enemyY >= 0){
						enemyY = enemyY - enespd;
					}
					break;
				case 0:
					//下キー
					if(windowHeight-310 > enemyY) {
						enemyY = enemyY + enespd;
					}
					break;
				case 3://左
					if(enemyX >= 0) {
						enemyX = enemyX - enespd;
					}
					break;
				case 1://右
					if(windowWidth-200 > enemyX) {
						enemyX = enemyX + enespd;
					}
					break;
				}
			}
		}

		/* 一定時間ごとの処理(ActionListenerに対する処理):相手の移動、スキル、補助アイテム*/
		public void actionPerformed(ActionEvent e) {
			if(enmatk==false) { icount++; }
			if(icount%10 == 0) {
				Random random = new Random();
				skilland = random.nextInt(3);
			}
			if(icount%80==0) {
				Random random = new Random();
				iconrand = random.nextInt(5);
				icontmp = 0;
			}
			repaint();
		}

		/* MouseListener に対する処理 */
		public void mouseClicked(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}
		/* MouseMotionListener に対する処理 */
		public void mouseMoved(MouseEvent e) {
		}

		public void mouseDragged(MouseEvent e) {
		}
		
		public void keyPressed(KeyEvent e) {
			move(e);
		}
		public void keyTyped(KeyEvent e) {
			move(e);
		}
		public void keyReleased(KeyEvent e) {
		}
		public void move(KeyEvent e) {
			switch ( e.getKeyCode() ) {
			case KeyEvent.VK_UP:
				//上キー
				if(myY >= 0){ 
					muki = 2;
					myY = myY - (myspd + buffmyspd);
				}
				break;
			case KeyEvent.VK_DOWN:
				//下キー
				if(windowHeight-150 > myY) {
					muki = 0;
					myY = myY + (myspd + buffmyspd);
				}
				break;
			case KeyEvent.VK_LEFT:
				//左キー
				if(myX >= 0) {
					muki = 3;
					myX = myX - (myspd + buffmyspd);
				}
				break;
			case KeyEvent.VK_RIGHT:
				//右キー
				if(windowWidth-100 > myX) {
					muki = 1;
					myX = myX + (myspd + buffmyspd);
				}
				break;
			case KeyEvent.VK_Z:
				if(iskey == false) atk = true;
				break;
			case KeyEvent.VK_X:
				if(skilldelay == -1 && iskey == false) dan = true;
				break;
			default:
				isMove = true;
				break;
			}
		}
	}
}
