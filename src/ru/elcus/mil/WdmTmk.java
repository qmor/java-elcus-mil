package ru.elcus.mil;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT.HANDLE;

import ru.elcus.mil.structs.TTmkConfigData;
import ru.elcus.mil.structs.TTmkEventData;
public class WdmTmk {
	static CWdmtmkInterface INSTANCE = (CWdmtmkInterface) Native.load((Platform.isWindows() ? "Wdmtmk" : "Wdmtmk"), CWdmtmkInterface.class);
	public static short POLLIN = 0x0001;

	interface CWdmtmkInterface extends Library {
		 int TmkOpen();
		 void TmkClose();
		 int tmkgetmaxn();
		 int tmkconfig(int tmkNumber );
		 int tmkdone(int tmkNumber );
		 int tmkselect(int tmkNumber );
		 int tmkselected();
		 short tmkgetmode();
		 void tmksetcwbits(int tmkSetControl);
		 void tmkclrcwbits(int tmkClrControl);
		 short tmkgetcwbits();
		 void tmkdefevent( HANDLE hEvent, int fEventSet );
		 void tmkgetevd( TTmkEventData pEvD );
		 void tmkgetinfo(TTmkConfigData pConfD);
		 int bcreset();
		 void bc_def_tldw(short wTLDW );
		 void bc_enable_di();
		 void bc_disable_di();
		 int bcdefirqmode(int bcIrqMode);
		 short bcgetirqmode();
		 short bcgetmaxbase();
		 int bcdefbase(int bcBasePC);
		 short bcgetbase();
		 void bcputw(int bcAddr, int bcData );
		 short bcgetw(int bcAddr );
		 int bcgetansw(int bcCtrlCode );
		 void bcputblk(int bcAddr, Pointer pcBuffer, int cwLength );
		 void bcgetblk(int bcAddr, Pointer pcBuffer, int cwLength );
		 int bcdefbus(int bcBus );
		 short bcgetbus();
		 int bcstart(int bcBase, int bcCtrlCode );
		 int bcstartx(int bcBase, int bcCtrlCode );
		 int bcdeflink(int bcBase, int bcCtrlCode );
		 int bcgetlink();
		 short bcstop();
		 int bcgetstate();
		 int rtreset();
		 int rtdefirqmode(int rtIrqMode );
		 short rtgetirqmode();
		 int rtdefmode(int rtMode );
		 short rtgetmode();
		 short rtgetmaxpage();
		 int rtdefpage(int rtPage );
		 int rtenable(int rtEnable);
		 short rtgetpage();
		 int rtdefpagepc(int rtPagePC );
		 int rtdefpagebus(int rtPageBus );
		 short rtgetpagepc();
		 short rtgetpagebus();
		 int rtdefaddress(int rtAddress );
		 short rtgetaddress();
		 void rtdefsubaddr( int rtDir, int rtSubAddr);
		 short rtgetsubaddr();
		 void rtputw(int rtAddr, int rtData );
		 short rtgetw(int rtAddr );
		 void rtputblk( int rtAddr, Pointer pcBuffer, int cwLength );
		 void rtgetblk( int rtAddr, Pointer pcBuffer, int cwLength );
		 void rtsetanswbits( short rtSetControl );
		 void rtclranswbits( short rtClrControl );
		 short rtgetanswbits();
		 void rtgetflags( Pointer pcBuffer, short rtDir, short rtFlagMin, short rtFlagMax );
		 void rtputflags( Pointer pcBuffer, short rtDir, short rtFlagMin, short rtFlagMax );
		 void rtsetflag();
		 void rtclrflag();
		 short rtgetflag( short rtDir, short rtSubAddr );
		 short rtgetstate();
		 int rtbusy();
		 void rtlock( short rtDir, short rtSubAddr );
		 void rtunlock();
		 int rtgetcmddata( short rtBusCommand );
		 void rtputcmddata(int rtBusCommand, int rtData );
		 int mtreset();
		 short mtgetsw();

	}
}
