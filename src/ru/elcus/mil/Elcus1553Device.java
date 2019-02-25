package ru.elcus.mil;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import ru.elcus.mil.structs.CC;
import ru.elcus.mil.structs.CLong2;
import ru.elcus.mil.structs.EMilFormat;
import ru.elcus.mil.structs.TTmkConfigData;
import ru.elcus.mil.structs.TTmkEventData;
/**
 * working with elcus mil1553 cards
 * @author qmor
 *
 */

public class Elcus1553Device {



	static final int TMK_VERSION_MIN =0x0403; /* v4.03 */
	static final int TMK_VERSION =0x0406 ;    /* v4.06 */

	static final int MIN_TMK_TYPE =2;
	static final int MAX_TMK_TYPE= 12;

	static final int TMK400 =2;
	static final int TMKMPC= 3;
	static final int RTMK400= 4;
	static final int TMKX= 5;
	static final int TMKXI =6;
	static final int MRTX =7;
	static final int MRTXI= 8;
	static final int TA =9;
	static final int TAI =10;
	static final int MRTA =11;
	static final int MRTAI =12;

	static final int ALL_TMKS =0x00FF;

	static final int GET_TIMEOUT =0xFFFF;

	static final int SWTIMER_OFF   =0x0000;
	static final int SWTIMER_ON    =0x2400;
	static final int SWTIMER_EVENT =0x8000;
	static final int SWTIMER_RESET =0xFBFF;

	static final int GET_SWTIMER_CTRL =0xFFFF;

	static final int TIMER_RESET =0xFBFF;
	static final int TIMER_OFF   =0x0000;
	static final int TIMER_16BIT =0x3400;
	static final int TIMER_32BIT =0x2400;
	static final int TIMER_1US   =0x0000;
	static final int TIMER_2US   =0x0080;
	static final int TIMER_4US   =0x0100;
	static final int TIMER_8US   =0x0180;
	static final int TIMER_16US  =0x0200;
	static final int TIMER_32US  =0x0280;
	static final int TIMER_64US  =0x0300;
	static final int TIMER_STOP  =0x0380;
	static final int TIMER_SYN   =0x0040;
	static final int TIMER_SYND  =0x0020;
	static final int TIMER_SA    =0x001F;

	static final int TIMER_NOSTOP =0x2000;

	static final int TIMER_MASK  =0x37FF;
	static final int TIMER_STEP  =0x0380;
	static final int TIMER_BITS  =0x3400;

	static final int GET_TIMER_CTRL =0xFFFF;

	static final int DATA_BC_RT =0x00;
	static final int DATA_BC_RT_BRCST =0x08;
	static final int DATA_RT_BC =0x01;
	static final int DATA_RT_RT =0x02;
	static final int DATA_RT_RT_BRCST =0x0A;
	static final int CTRL_C_A =0x03;
	static final int CTRL_C_BRCST =0x0B;
	static final int CTRL_CD_A =0x04;
	static final int CTRL_CD_BRCST =0x0C;
	static final int CTRL_C_AD =0x05;

	static final int BUS_A =0;
	static final int BUS_B =1;
	static final int BUS_1 =0;
	static final int BUS_2 =1;

	static final int S_ERAO_MASK =0x01;
	static final int S_MEO_MASK =0x02;
	static final int S_IB_MASK =0x04;
	static final int S_TO_MASK= 0x08;
	static final int S_EM_MASK =0x10;
	static final int S_EBC_MASK =0x20;
	static final int S_DI_MASK =0x40;
	static final int S_ELN_MASK =0x80;
	static final int S_G1_MASK =0x1000;
	static final int S_G2_MASK =0x2000;

	static final int NWORDS_MASK =0x001F;
	static final int CMD_MASK =0x001F;
	static final int SUBADDR_MASK =0x03E0;
	static final int CI_MASK =0x03E0;
	static final int HBIT_MASK =0x0200;
	static final int RT_DIR_MASK =0x0400;
	static final int ADDRESS_MASK =0xF800;
	static final int RTFL_MASK =0x0001;
	static final int DNBA_MASK =0x0002;
	static final int SSFL_MASK =0x0004;
	static final int BUSY_MASK =0x0008;
	static final int BRCST_MASK= 0x0010;
	static final int NULL_MASK =0x00E0;
	static final int SREQ_MASK =0x0100;
	static final int ERROR_MASK =0x0400;

	static final int SREQ =0x01;
	static final int BUSY =0x02;
	static final int SSFL =0x04;
	static final int RTFL =0x08;
	static final int DNBA =0x10;

	static final int CWB0 =0x20;
	
	static final int CWB1 =0x40;

	static final int BC_MODE =0x00;
	static final int RT_MODE =0x80;
	static final int MT_MODE =0x100;
	static final int MRT_MODE =0x280;
	static final int UNDEFINED_MODE =0xFFFF;

	static final int RT_TRANSMIT =0x0400;
	static final int RT_RECEIVE =0x0000;

	static final int RT_ERROR_MASK =0x4000;

	static final int RT_FLAG =0x8000;
	static final int RT_FLAG_MASK =0x8000;

	static final int RT_HBIT_MODE =0x0001;
	static final int RT_FLAG_MODE =0x0002;
	static final int RT_BRCST_MODE =0x0004;
	static final int RT_DATA_BL =0x2000;
	public static final int RT_GENER1_BL =0x0004;
	public static final int RT_GENER2_BL =0x4000;
	static final int BC_GENER1_BL =0x0004;
	static final int BC_GENER2_BL =0x4000;
	static final int MT_GENER1_BL =0x0004;
	static final int MT_GENER2_BL =0x4000;
	static final int TMK_IRQ_OFF =0x8000;

	static final int CX_CC_MASK =0x000F;
	static final int CX_CONT_MASK =0x0010;
	static final int CX_BUS_MASK =0x0020;
	static final int CX_SIG_MASK =0x8000;
	static final int CX_INT_MASK =0x0020;

	public static final int CX_CONT =0x0010;
	static final int CX_STOP =0x0000;
	static final int CX_BUS_0 =0x0000;
	static final int CX_BUS_A =0x0000;
	static final int CX_BUS_1 =0x0020;
	static final int CX_BUS_B =0x0020;
	static final int CX_NOSIG =0x0000;
	public static final int CX_SIG =0x8000;
	public static final int CX_INT =0x0000;
	public static final int CX_NOINT =0x0020;

	static final int SX_NOERR =0;
	static final int SX_MEO =1;
	static final int SX_TOA =2;
	static final int SX_TOD =3;
	static final int SX_ELN =4;
	static final int SX_ERAO =5;
	static final int SX_ESYN =6;
	static final int SX_EBC =7;

	static final int SX_ERR_MASK =0x0007;
	static final int SX_IB_MASK =0x0008;
	static final int SX_G1_MASK =0x0010;
	static final int SX_G2_MASK =0x0020;
	static final int SX_K2_MASK =0x0100;
	static final int SX_K1_MASK =0x0200;
	static final int SX_SCC_MASK =0x3C00;
	static final int SX_ME_MASK =0x4000;
	static final int SX_BUS_MASK =0x8000;

	static final int SX_BUS_0 =0x0000;
	static final int SX_BUS_A =0x0000;
	static final int SX_BUS_1 =0x8000;
	static final int SX_BUS_B =0x8000;

	static final int GET_IO_DELAY =0xFFFF;

	static final int RT_ENABLE =0x0000;
	static final int RT_DISABLE =0x001F;
	static final int RT_GET_ENABLE =0xFFFF;

	static final int CW(int ADDR,int DIR,int SUBADDR,int NWORDS)
	{
		return (((ADDR)<<11)|(DIR)|((SUBADDR)<<5)|((NWORDS)&0x1F));	
	}
	static final int CWM(int ADDR,int COMMAND) 
	{ 
		return (((ADDR)<<11)|(CI_MASK)|(COMMAND));
	}
	static final int CWMC(int ADDR,int CI,int COMMAND)
	{
		return (((ADDR)<<11)|((CI)&0x03E0)|(COMMAND));	
	}

	/*static final int CMD_ILLEGAL 0x000*/
	static final int CMD_DYNAMIC_BUS_CONTROL =0x400;
	static final int CMD_SYNCHRONIZE =0x401;
	static final int CMD_TRANSMIT_STATUS_WORD =0x402;
	static final int CMD_INITIATE_SELF_TEST =0x403;
	static final int CMD_TRANSMITTER_SHUTDOWN =0x404;
	static final int CMD_OVERRIDE_TRANSMITTER_SHUTDOWN =0x405;
	static final int CMD_INHIBIT_TERMINAL_FLAG_BIT =0x406;
	static final int CMD_OVERRIDE_INHIBIT_TERMINAL_FLAG_BIT =0x407;
	static final int CMD_RESET_REMOTE_TERMINAL =0x408;
	static final int CMD_TRANSMIT_VECTOR_WORD =0x410;
	static final int CMD_SYNCHRONIZE_WITH_DATA_WORD =0x011;
	static final int CMD_TRANSMIT_LAST_COMMAND_WORD =0x412;
	static final int CMD_TRANSMIT_BUILT_IN_TEST_WORD =0x413;


	static final int TMK_BAD_0       = -1024;
	static final int TMK_BAD_TYPE    = (TMK_BAD_0-1);
	static final int TMK_BAD_IRQ     = (TMK_BAD_0-2);
	static final int TMK_BAD_NUMBER  = (TMK_BAD_0-3);
	static final int BC_BAD_BUS      = (TMK_BAD_0-4);
	static final int BC_BAD_BASE     = (TMK_BAD_0-5);
	static final int BC_BAD_LEN      = (TMK_BAD_0-6);
	static final int RT_BAD_PAGE     = (TMK_BAD_0-7);
	static final int RT_BAD_LEN      = (TMK_BAD_0-8);
	static final int RT_BAD_ADDRESS  = (TMK_BAD_0-9);
	static final int RT_BAD_FUNC     = (TMK_BAD_0-10);
	static final int BC_BAD_FUNC      =(TMK_BAD_0-11);
	static final int TMK_BAD_FUNC     =(TMK_BAD_0-12);
	static final int VTMK_BAD_VERSION =(TMK_BAD_0-13);




	static final int TMK_IOC_MAGIC  ='k';
	static final int TMK_IOC0 =0;

	static final int VTMK_tmkconfig =2;
	static final int VTMK_tmkdone= 3;
	static final int VTMK_tmkgetmaxn =4;
	static final int VTMK_tmkselect =5;
	static final int VTMK_tmkselected =6;
	static final int VTMK_tmkgetmode =7;
	static final int VTMK_tmksetcwbits =8;
	static final int VTMK_tmkclrcwbits =9;
	static final int VTMK_tmkgetcwbits= 10;
	static final int VTMK_tmkwaitevents =11;
	//static final int VTMK_tmkdefevent 11
	static final int VTMK_tmkgetevd =12;

	static final int VTMK_bcreset =13;
	static final int VTMK_bc_def_tldw =14;
	static final int VTMK_bc_enable_di =15;
	static final int VTMK_bc_disable_di =16;
	static final int VTMK_bcdefirqmode= 17;
	static final int VTMK_bcgetirqmode =18;
	static final int VTMK_bcgetmaxbase =19;
	static final int VTMK_bcdefbase =20;
	static final int VTMK_bcgetbase =21;
	static final int VTMK_bcputw =22;
	static final int VTMK_bcgetw =23;
	static final int VTMK_bcgetansw =24;
	static final int VTMK_bcputblk =25;
	static final int VTMK_bcgetblk =26;
	static final int VTMK_bcdefbus =27;
	static final int VTMK_bcgetbus =28;
	static final int VTMK_bcstart =29;
	static final int VTMK_bcstartx= 30;
	static final int VTMK_bcdeflink =31;
	static final int VTMK_bcgetlink =32;
	static final int VTMK_bcstop =33;
	static final int VTMK_bcgetstate =34;

	static final int VTMK_rtreset= 35;
	static final int VTMK_rtdefirqmode =36;
	static final int VTMK_rtgetirqmode =37;
	static final int VTMK_rtdefmode= 38;
	static final int VTMK_rtgetmode= 39;
	static final int VTMK_rtgetmaxpage =40;
	static final int VTMK_rtdefpage =41;
	static final int VTMK_rtgetpage =42;
	static final int VTMK_rtdefpagepc= 43;
	static final int VTMK_rtdefpagebus =44;
	static final int VTMK_rtgetpagepc= 45;
	static final int VTMK_rtgetpagebus =46;
	static final int VTMK_rtdefaddress =47;
	static final int VTMK_rtgetaddress =48;
	static final int VTMK_rtdefsubaddr= 49;
	static final int VTMK_rtgetsubaddr =50;
	static final int VTMK_rtputw =51;
	static final int VTMK_rtgetw =52;
	static final int VTMK_rtputblk =53;
	static final int VTMK_rtgetblk =54;
	static final int VTMK_rtsetanswbits= 55;
	static final int VTMK_rtclranswbits =56;
	static final int VTMK_rtgetanswbits =57;
	static final int VTMK_rtgetflags= 58;
	static final int VTMK_rtputflags =59;
	static final int VTMK_rtsetflag =60;
	static final int VTMK_rtclrflag =61;
	static final int VTMK_rtgetflag =62;
	static final int VTMK_rtgetstate =63;
	static final int VTMK_rtbusy =64;
	static final int VTMK_rtlock= 65;
	static final int VTMK_rtunlock= 66;
	static final int VTMK_rtgetcmddata =67;
	static final int VTMK_rtputcmddata= 68;

	static final int VTMK_mtreset =69;
	static final int VTMK_mtdefirqmode= 70;
	static final int VTMK_mtgetirqmode= 71;
	static final int VTMK_mtgetmaxbase =72;
	static final int VTMK_mtdefbase =73;
	static final int VTMK_mtgetbase= 74;
	static final int VTMK_mtputw =75;
	static final int VTMK_mtgetw =76;
	static final int VTMK_mtgetsw= 77;
	static final int VTMK_mtputblk= 78;
	static final int VTMK_mtgetblk =79;
	static final int VTMK_mtstartx= 80;
	static final int VTMK_mtdeflink= 81;
	static final int VTMK_mtgetlink =82;
	static final int VTMK_mtstop= 83;
	static final int VTMK_mtgetstate= 84;

	static final int VTMK_tmkgetinfo= 85;
	static final int VTMK_GetVersion= 86;

	static final int VTMK_rtenable= 87;

	static final int VTMK_mrtgetmaxn =88;
	static final int VTMK_mrtconfig =89;
	static final int VTMK_mrtselected =90;
	static final int VTMK_mrtgetstate= 91;
	static final int VTMK_mrtdefbrcsubaddr0= 92;
	static final int VTMK_mrtreset =93;

	static final int VTMK_tmktimer= 94;
	static final int VTMK_tmkgettimer =95;
	static final int VTMK_tmkgettimerl =96;
	static final int VTMK_bcgetmsgtime =97;
	static final int VTMK_mtgetmsgtime =98;
	static final int VTMK_rtgetmsgtime= 99;

	static final int VTMK_tmkgethwver =100;

	static final int VTMK_tmkgetevtime =101;
	static final int VTMK_tmkswtimer= 102;
	static final int VTMK_tmkgetswtimer= 103;

	static final int VTMK_tmktimeout =104;

	static final int VTMK_mrtdefbrcpage =105;
	static final int VTMK_mrtgetbrcpage =106;

	static final int TMK_IOC_MAXNR =106;
	static final int  TMK_IOCGetVersion = ioctl._IO(TMK_IOC_MAGIC, VTMK_GetVersion+TMK_IOC0);
	static final int TMK_IOCmrtdefbrcpage = ioctl._IO(TMK_IOC_MAGIC, VTMK_mrtdefbrcpage+TMK_IOC0);

	/*#define mtdefirqmode bcdefirqmode
	#define mtgetirqmode bcgetirqmode
	#define mtgetmaxbase bcgetmaxbase
	#define mtdefbase bcdefbase
	#define mtgetbase bcgetbase
	#define mtputw bcputw
	#define mtgetw bcgetw
	#define mtputblk bcputblk
	#define mtgetblk bcgetblk
	#define mtstartx bcstartx
	#define mtdeflink bcdeflink
	#define mtgetlink bcgetlink
	#define mtstop bcstop
	#define mtgetstate bcgetstate
	#define mtgetmsgtime bcgetmsgtime*/

	static int _hVTMK4VxD = 0;

	static final int  TMK_IOCtmkconfig = ioctl._IO(TMK_IOC_MAGIC, VTMK_tmkconfig+TMK_IOC0);
	static final int  TMK_IOCtmkdone = ioctl. _IO(TMK_IOC_MAGIC, VTMK_tmkdone+TMK_IOC0);
	static final int  TMK_IOCtmkgetmaxn  = ioctl._IO(TMK_IOC_MAGIC, VTMK_tmkgetmaxn+TMK_IOC0);
	static final int  TMK_IOCtmkselect  = ioctl._IO(TMK_IOC_MAGIC, VTMK_tmkselect+TMK_IOC0);
	static final int  TMK_IOCtmkselected  = ioctl._IO(TMK_IOC_MAGIC, VTMK_tmkselected+TMK_IOC0);
	static final int  TMK_IOCtmkgetmode  = ioctl._IO(TMK_IOC_MAGIC, VTMK_tmkgetmode+TMK_IOC0);
	static final int  TMK_IOCtmksetcwbits  = ioctl._IO(TMK_IOC_MAGIC, VTMK_tmksetcwbits+TMK_IOC0);
	static final int  TMK_IOCtmkclrcwbits  = ioctl._IO(TMK_IOC_MAGIC, VTMK_tmkclrcwbits+TMK_IOC0);
	static final int  TMK_IOCtmkgetcwbits  = ioctl._IO(TMK_IOC_MAGIC, VTMK_tmkgetcwbits+TMK_IOC0);
	static final int  TMK_IOCtmkwaitevents  = ioctl._IOW(TMK_IOC_MAGIC, VTMK_tmkwaitevents+TMK_IOC0, 8);

	static final int  TMK_IOCtmkgetevd  = ioctl._IOR(TMK_IOC_MAGIC, VTMK_tmkgetevd+TMK_IOC0, 22);

	static final int  TMK_IOCbcreset  = ioctl._IO(TMK_IOC_MAGIC, VTMK_bcreset+TMK_IOC0);
	//static final int  TMK_IOCbc_def_tldw _IO(TMK_IOC_MAGIC, VTMK_bc_def_tldw+TMK_IOC0)
	//static final int  TMK_IOCbc_enable_di _IO(TMK_IOC_MAGIC, VTMK_bc_enable_di+TMK_IOC0)
	//static final int  TMK_IOCbc_disable_di _IO(TMK_IOC_MAGIC, VTMK_bc_disable_di+TMK_IOC0)
	static final int  TMK_IOCbcdefirqmode  = ioctl._IO(TMK_IOC_MAGIC, VTMK_bcdefirqmode+TMK_IOC0);
	static final int  TMK_IOCbcgetirqmode  = ioctl._IO(TMK_IOC_MAGIC, VTMK_bcgetirqmode+TMK_IOC0);
	static final int  TMK_IOCbcgetmaxbase  = ioctl._IO(TMK_IOC_MAGIC, VTMK_bcgetmaxbase+TMK_IOC0);
	static final int  TMK_IOCbcdefbase  = ioctl._IO(TMK_IOC_MAGIC, VTMK_bcdefbase+TMK_IOC0);
	static final int  TMK_IOCbcgetbase  = ioctl._IO(TMK_IOC_MAGIC, VTMK_bcgetbase+TMK_IOC0);
	static final int  TMK_IOCbcputw  = ioctl._IO(TMK_IOC_MAGIC, VTMK_bcputw+TMK_IOC0);
	static final int  TMK_IOCbcgetw  = ioctl._IO(TMK_IOC_MAGIC, VTMK_bcgetw+TMK_IOC0);
	static final int  TMK_IOCbcgetansw  = ioctl._IOWR(TMK_IOC_MAGIC, VTMK_bcgetansw+TMK_IOC0, 4);
	static final int  TMK_IOCbcputblk  = ioctl._IOW(TMK_IOC_MAGIC, VTMK_bcputblk+TMK_IOC0, 16);
	static final int  TMK_IOCbcgetblk  = ioctl._IOW(TMK_IOC_MAGIC, VTMK_bcgetblk+TMK_IOC0, 16);
	static final int  TMK_IOCbcdefbus  = ioctl._IO(TMK_IOC_MAGIC, VTMK_bcdefbus+TMK_IOC0);
	static final int  TMK_IOCbcgetbus  = ioctl._IO(TMK_IOC_MAGIC, VTMK_bcgetbus+TMK_IOC0);
	static final int  TMK_IOCbcstart  = ioctl._IO(TMK_IOC_MAGIC, VTMK_bcstart+TMK_IOC0);
	static final int  TMK_IOCbcstartx  = ioctl._IO(TMK_IOC_MAGIC, VTMK_bcstartx+TMK_IOC0);
	static final int  TMK_IOCbcdeflink  = ioctl._IO(TMK_IOC_MAGIC, VTMK_bcdeflink+TMK_IOC0);
	static final int  TMK_IOCbcgetlink  = ioctl._IOR(TMK_IOC_MAGIC, VTMK_bcgetlink+TMK_IOC0, 4);
	static final int  TMK_IOCbcstop  = ioctl._IO(TMK_IOC_MAGIC, VTMK_bcstop+TMK_IOC0);
	static final int  TMK_IOCbcgetstate  = ioctl._IOR(TMK_IOC_MAGIC, VTMK_bcgetstate+TMK_IOC0, 4);

	static final int  TMK_IOCrtreset  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtreset+TMK_IOC0);
	static final int  TMK_IOCrtdefirqmode  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtdefirqmode+TMK_IOC0);
	static final int  TMK_IOCrtgetirqmode  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtgetirqmode+TMK_IOC0);
	static final int  TMK_IOCrtdefmode  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtdefmode+TMK_IOC0);
	static final int  TMK_IOCrtgetmode  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtgetmode+TMK_IOC0);
	static final int  TMK_IOCrtgetmaxpage  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtgetmaxpage+TMK_IOC0);
	static final int  TMK_IOCrtdefpage  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtdefpage+TMK_IOC0);
	static final int  TMK_IOCrtgetpage  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtgetpage+TMK_IOC0);
	static final int  TMK_IOCrtdefpagepc  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtdefpagepc+TMK_IOC0);
	static final int  TMK_IOCrtdefpagebus  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtdefpagebus+TMK_IOC0);
	static final int  TMK_IOCrtgetpagepc  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtgetpagepc+TMK_IOC0);
	static final int  TMK_IOCrtgetpagebus  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtgetpagebus+TMK_IOC0);
	static final int  TMK_IOCrtdefaddress  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtdefaddress+TMK_IOC0);
	static final int  TMK_IOCrtgetaddress  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtgetaddress+TMK_IOC0);
	static final int  TMK_IOCrtdefsubaddr  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtdefsubaddr+TMK_IOC0);
	static final int  TMK_IOCrtgetsubaddr  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtgetsubaddr+TMK_IOC0);
	static final int  TMK_IOCrtputw  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtputw+TMK_IOC0);
	static final int  TMK_IOCrtgetw  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtgetw+TMK_IOC0);
	static final int  TMK_IOCrtputblk  = ioctl._IOW(TMK_IOC_MAGIC, VTMK_rtputblk+TMK_IOC0, 16);
	static final int  TMK_IOCrtgetblk  = ioctl._IOW(TMK_IOC_MAGIC, VTMK_rtgetblk+TMK_IOC0, 16);
	static final int  TMK_IOCrtsetanswbits  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtsetanswbits+TMK_IOC0);
	static final int  TMK_IOCrtclranswbits  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtclranswbits+TMK_IOC0);
	static final int  TMK_IOCrtgetanswbits  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtgetanswbits+TMK_IOC0);
	static final int  TMK_IOCrtgetflags  = ioctl._IOW(TMK_IOC_MAGIC, VTMK_rtgetflags+TMK_IOC0, 16);
	static final int  TMK_IOCrtputflags  = ioctl._IOW(TMK_IOC_MAGIC, VTMK_rtputflags+TMK_IOC0, 16);
	static final int  TMK_IOCrtsetflag  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtsetflag+TMK_IOC0);
	static final int  TMK_IOCrtclrflag  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtclrflag+TMK_IOC0);
	static final int  TMK_IOCrtgetflag  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtgetflag+TMK_IOC0);
	static final int  TMK_IOCrtgetstate  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtgetstate+TMK_IOC0);
	static final int  TMK_IOCrtbusy  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtbusy+TMK_IOC0);
	static final int  TMK_IOCrtlock  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtlock+TMK_IOC0);
	static final int  TMK_IOCrtunlock  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtunlock+TMK_IOC0);
	static final int  TMK_IOCrtgetcmddata  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtgetcmddata+TMK_IOC0);
	static final int  TMK_IOCrtputcmddata  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtputcmddata+TMK_IOC0);

	static final int  TMK_IOCmtreset  = ioctl._IO(TMK_IOC_MAGIC, VTMK_mtreset+TMK_IOC0);
	static final int  TMK_IOCmtdefirqmode  = ioctl._IO(TMK_IOC_MAGIC, VTMK_mtdefirqmode+TMK_IOC0);
	static final int  TMK_IOCmtgetirqmode  = ioctl._IO(TMK_IOC_MAGIC, VTMK_mtgetirqmode+TMK_IOC0);
	static final int  TMK_IOCmtgetmaxbase  = ioctl._IO(TMK_IOC_MAGIC, VTMK_mtgetmaxbase+TMK_IOC0);
	static final int  TMK_IOCmtdefbase  = ioctl._IO(TMK_IOC_MAGIC, VTMK_mtdefbase+TMK_IOC0);
	static final int  TMK_IOCmtgetbase  = ioctl._IO(TMK_IOC_MAGIC, VTMK_mtgetbase+TMK_IOC0);
	static final int  TMK_IOCmtputw  = ioctl._IO(TMK_IOC_MAGIC, VTMK_mtputw+TMK_IOC0);
	static final int  TMK_IOCmtgetw  = ioctl._IO(TMK_IOC_MAGIC, VTMK_mtgetw+TMK_IOC0);
	static final int  TMK_IOCmtgetsw  = ioctl._IO(TMK_IOC_MAGIC, VTMK_mtgetsw+TMK_IOC0);
	static final int  TMK_IOCmtputblk  = ioctl._IOW(TMK_IOC_MAGIC, VTMK_mtputblk+TMK_IOC0, 16);
	static final int  TMK_IOCmtgetblk  = ioctl._IOW(TMK_IOC_MAGIC, VTMK_mtgetblk+TMK_IOC0, 16);
	static final int  TMK_IOCmtstartx  = ioctl._IO(TMK_IOC_MAGIC, VTMK_mtstartx+TMK_IOC0);
	static final int  TMK_IOCmtdeflink  = ioctl._IO(TMK_IOC_MAGIC, VTMK_mtdeflink+TMK_IOC0);
	static final int  TMK_IOCmtgetlink  = ioctl._IOR(TMK_IOC_MAGIC, VTMK_mtgetlink+TMK_IOC0, 4);
	static final int  TMK_IOCmtstop  = ioctl._IO(TMK_IOC_MAGIC, VTMK_mtstop+TMK_IOC0);
	static final int  TMK_IOCmtgetstate  = ioctl._IOR(TMK_IOC_MAGIC, VTMK_mtgetstate+TMK_IOC0, 4);

	static final int  TMK_IOCtmkgetinfo  = ioctl._IOR(TMK_IOC_MAGIC, VTMK_tmkgetinfo+TMK_IOC0, 22);

	static final int  TMK_IOCrtenable  = ioctl._IO(TMK_IOC_MAGIC, VTMK_rtenable+TMK_IOC0);

	static final int  TMK_IOCmrtgetmaxn  = ioctl._IO(TMK_IOC_MAGIC, VTMK_mrtgetmaxn+TMK_IOC0);
	static final int  TMK_IOCmrtconfig  = ioctl._IO(TMK_IOC_MAGIC, VTMK_mrtconfig+TMK_IOC0);
	static final int  TMK_IOCmrtselected  = ioctl._IO(TMK_IOC_MAGIC, VTMK_mrtselected+TMK_IOC0);
	static final int  TMK_IOCmrtgetstate  = ioctl._IO(TMK_IOC_MAGIC, VTMK_mrtgetstate+TMK_IOC0);
	static final int  TMK_IOCmrtdefbrcsubaddr0  = ioctl._IO(TMK_IOC_MAGIC, VTMK_mrtdefbrcsubaddr0+TMK_IOC0);
	static final int  TMK_IOCmrtreset  = ioctl._IO(TMK_IOC_MAGIC, VTMK_mrtreset+TMK_IOC0);

	static final int  TMK_IOCtmktimer  = ioctl._IO(TMK_IOC_MAGIC, VTMK_tmktimer+TMK_IOC0);
	static final int  TMK_IOCtmkgettimer  = ioctl._IOR(TMK_IOC_MAGIC, VTMK_tmkgettimer+TMK_IOC0, 4);
	static final int  TMK_IOCtmkgettimerl  = ioctl._IO(TMK_IOC_MAGIC, VTMK_tmkgettimerl+TMK_IOC0);
	static final int  TMK_IOCbcgetmsgtime  = ioctl._IOR(TMK_IOC_MAGIC, VTMK_bcgetmsgtime+TMK_IOC0, 4);
	static final int  TMK_IOCmtgetmsgtime  = ioctl._IOR(TMK_IOC_MAGIC, VTMK_mtgetmsgtime+TMK_IOC0, 4);
	static final int  TMK_IOCrtgetmsgtime  = ioctl._IOR(TMK_IOC_MAGIC, VTMK_rtgetmsgtime+TMK_IOC0, 4);

	static final int  TMK_IOCtmkgethwver  = ioctl._IO(TMK_IOC_MAGIC, VTMK_tmkgethwver+TMK_IOC0);

	static final int  TMK_IOCtmkgetevtime  = ioctl._IOR(TMK_IOC_MAGIC, VTMK_tmkgetevtime+TMK_IOC0, 4);
	static final int  TMK_IOCtmkswtimer  = ioctl._IO(TMK_IOC_MAGIC, VTMK_tmkswtimer+TMK_IOC0);
	static final int  TMK_IOCtmkgetswtimer  = ioctl._IOR(TMK_IOC_MAGIC, VTMK_tmkgetswtimer+TMK_IOC0, 4);

	static final int  TMK_IOCtmktimeout  = ioctl._IO(TMK_IOC_MAGIC, VTMK_tmktimeout+TMK_IOC0);

	static final int  TMK_IOCmrtgetbrcpage  = ioctl._IO(TMK_IOC_MAGIC, VTMK_mrtgetbrcpage+TMK_IOC0);

	boolean paused=true;
	int mtLastBase = 0;
	private Thread runnerThread;

	private boolean threadRunning = false;
	private int cardNumber = 0;
	public int getCardNumber() {
		return cardNumber;
	}

	private List<IMilMsgReceivedListener> msgReceivedListeners = new ArrayList<>();
	private List<DebugReceivedListener> DebugReceivedListeners = new ArrayList<>();
	private List<Mil1553Packet> packetsForSendBC = new ArrayList<>();	
	private Integer rtAddress=0;
	private boolean initiliased = false;
	private static Object syncObject = new Object();
	private int mtMaxBase;
	MilWorkMode workMode = MilWorkMode.eMilWorkModeNotSetted;
	
	public MilWorkMode getWorkMode() {
		return workMode;
	}


	public void addMsgReceivedListener(IMilMsgReceivedListener listener)
	{
		msgReceivedListeners.add(listener);
	}
	
	public void addDebugReceivedListener(DebugReceivedListener listener)
	{
		DebugReceivedListeners.add(listener);
	}
	
	public void setPause(boolean pause) throws Eclus1553Exception
	{
		if (workMode.equals(MilWorkMode.eMilWorkModeRT))
		{
			synchronized (syncObject) {
				int result  = tmkselect();
				if (result!=0)
					throw new Eclus1553Exception(this,"Ошибка tmkselect в функции setPause() "+result); 
				
				if(paused != pause)
				{
					result = rtenable(pause?RT_DISABLE:RT_ENABLE);
					paused = !paused;
				}
				if (result!=0)
				{
					throw new Eclus1553Exception(this,"Ошибка rtdefaddress в функции setRtAddress() "+result); 
				}
			}
		}

		else if(workMode.equals(MilWorkMode.eMilWorkModeMT))
		{
			synchronized (syncObject) {
				int result  = tmkselect();
				if (result!=0)
					throw new Eclus1553Exception(this,"Ошибка tmkselect в функции setRtAddress() "+result); 

				result = (pause) ? mtstop() : mtstartx(mtLastBase, (CX_CONT | CX_NOINT | CX_NOSIG));

				if (result!=0)
				{
					throw new Eclus1553Exception(this,"Ошибка rtdefaddress в функции setPause() "+result); 
				}
			}
		}
	}
	public void setRtAddress(Integer rtAddress) throws Eclus1553Exception
	{
		this.rtAddress = rtAddress;
		if (workMode==MilWorkMode.eMilWorkModeRT)
		{
			synchronized (syncObject) {
				int result  = tmkselect();
				if (result!=0)
					throw new Eclus1553Exception(this,"Ошибка tmkselect в функции setRtAddress() "+result); 
				result = rtdefaddress(rtAddress);
				if (result!=0)
				{
					throw new Eclus1553Exception(this,"Ошибка rtdefaddress в функции setRtAddress() "+result); 
				}
			}
		}
	}
	private Pointer rtSendPBuffer = new Memory(64*2);
	private void sendPacketRT(Mil1553Packet packet)
	{
		synchronized (syncObject) 
		{
			int result =  tmkselect();
			Integer subaddressMode  =  Mil1553Packet.getSubAddress(packet.commandWord);
			Integer rtrBit = Mil1553Packet.getRTRBit(packet.commandWord);
			Integer wordcountModeCode = Mil1553Packet.getWordsCount(packet.commandWord);
			if (rtrBit==1)
			{				
				if (subaddressMode!=0 && subaddressMode!=31)
				{
					if (wordcountModeCode == 0) wordcountModeCode=32;
					rtdefsubaddr(RT_TRANSMIT,subaddressMode);
					while(rtbusy()==1)
					{
						for (DebugReceivedListener listener: DebugReceivedListeners)
						{
							listener.msgReceived("Wait for rtbusy");
						}
					}
					rtSendPBuffer.write(0, packet.dataWords, 0, 32);
					rtputblk(0,rtSendPBuffer,wordcountModeCode);
				}
				else //if Mode
				{
					rtputcmddata(packet.commandWord & ((1<<10) | 31), (int)packet.dataWords[0]); // first dataword is for CMD data
				}
			}
		}
	}
			
	public void sendPacket(Mil1553Packet packet)
	{
		if (workMode == MilWorkMode.eMilWorkModeRT)
		{
			sendPacketRT(packet);
		}
		else if(workMode == MilWorkMode.eMilWorkModeBC)
		{
			packetsForSendBC.add(packet);
		}
	}
	private void listenLoopBC()
	{
		int events;
		TTmkEventData eventData = new TTmkEventData();
		Mil1553Packet Msg = new Mil1553Packet();
		Pointer pBuffer = new Memory(64*2);		
		while(threadRunning)
		{
			events = tmkwaitevents(1<<cardNumber, 50);
			if (events==(1<<cardNumber))
			{
				synchronized (syncObject) {	
					tmkselect();
					tmkgetevd(eventData);
					bcdefbase(0);
					Msg.commandWord = (short) bcgetw(0);
					Msg.format = Mil1553Packet.calcFormat(Msg.commandWord);
					Msg.date = LocalDateTime.now();
					Integer cmdcodeWordCount = Mil1553Packet.getWordsCount(Msg.commandWord);
					switch(Msg.format)
					{
					case CC_FMT_1:
						bcgetblk(1,pBuffer,cmdcodeWordCount);
						Msg.dataWords = pBuffer.getShortArray(0, 32);
						Msg.answerWord = (short) bcgetw(1+cmdcodeWordCount);
						break;
					case CC_FMT_2:
						bcgetblk(2,pBuffer,cmdcodeWordCount);
						Msg.dataWords = pBuffer.getShortArray(0, 32);
						Msg.answerWord = (short) bcgetw(1);
						break;
					case CC_FMT_4:
						Msg.answerWord = (short) bcgetw(1);
						break;
					case CC_FMT_5:
						Msg.answerWord = (short) bcgetw(1);
						Msg.dataWords[0] = (short) bcgetw(2);
						break;
					case CC_FMT_6:
						Msg.answerWord = (short) bcgetw(2);
						Msg.dataWords[0] = (short) bcgetw(1);						
						break;
					default :
						break;
					}
					if (eventData.nInt==1)
					{
						if (!packetsForSendBC.isEmpty())
							packetsForSendBC.remove(0);

						Msg.status = EMilPacketStatus.eRECEIVED;
					}
					
					if (eventData.nInt==2)
					{
						Msg.status = EMilPacketStatus.eFAILED;																		
						if (!packetsForSendBC.isEmpty())
						{
							packetsForSendBC.clear();
						}
	                    if (eventData.union.bc.wResult==S_ERAO_MASK)
							for (DebugReceivedListener listener: DebugReceivedListeners)
							{
								listener.msgReceived("The error in a field of the address received RW is found out");
							}	
	                    else if (eventData.union.bc.wResult == S_MEO_MASK)
							for (DebugReceivedListener listener: DebugReceivedListeners)
							{
								listener.msgReceived("The error of a code 'Manchester - 2' is found out at answer RT");
							}	                        	
						else if (eventData.union.bc.wResult == S_EBC_MASK)
							for (DebugReceivedListener listener: DebugReceivedListeners)
							{
								listener.msgReceived("The error of the echo - control over transfer BC is found out");
							}	
	                    else if (eventData.union.bc.wResult == S_TO_MASK)
							for (DebugReceivedListener listener: DebugReceivedListeners)
							{
								listener.msgReceived("It is not received the answer from RT");
							}	
	                    else if (eventData.union.bc.wResult == S_IB_MASK)
							for (DebugReceivedListener listener: DebugReceivedListeners)
							{
								listener.msgReceived("The established bits in received RW are found out");
							}
					}	
					for (IMilMsgReceivedListener listener: msgReceivedListeners)
					{
						listener.msgReceived(Msg);
					}	
				}
			}
			if (!packetsForSendBC.isEmpty())
			{
				Mil1553Packet msg = packetsForSendBC.get(0);
				msg.format = Mil1553Packet.calcFormat(msg.commandWord);
				synchronized (syncObject) {
					tmkselect();
					if (msg.format == EMilFormat.CC_FMT_1)
					{
						pBuffer.setShort(0, msg.commandWord);
						for (int i=0;i<32;i++)
							pBuffer.setShort(i+1, msg.dataWords[i]);

						bcdefbase(0);
						bcputblk(0, pBuffer, (short)64);
						bcdefbus(msg.bus.toInt());
						bcstart(0, DATA_BC_RT);
						msg.status = EMilPacketStatus.eSENT;
					}
					else if (msg.format.equals(EMilFormat.CC_FMT_2))
					{
						bcdefbase(0);
						bcputw(0,msg.commandWord);
						bcdefbus(msg.bus.toInt());
						bcstart(0,DATA_RT_BC);
						msg.status = EMilPacketStatus.eSENT;
					}
					else if (msg.format.equals(EMilFormat.CC_FMT_4) || msg.format.equals(EMilFormat.CC_FMT_5))
					{
						bcdefbase(0);
						bcputw(0,msg.commandWord);
						bcdefbus((msg.bus.toInt()));
						bcstart(0,msg.format.asInteger());
						msg.status = EMilPacketStatus.eSENT;
					}

					else if (msg.format.equals(EMilFormat.CC_FMT_6))
					{
						bcdefbase(0);
						bcputw(0,msg.commandWord);
						bcputw(1,msg.dataWords[0]);
						bcdefbus(msg.bus.toInt());
						bcstart(0,msg.format.asInteger());
						msg.status = EMilPacketStatus.eSENT;
					}
					else
					{
						packetsForSendBC.remove(0);
						for (DebugReceivedListener listener: DebugReceivedListeners)
						{
							listener.msgReceived("Exchange format is not realized yet");
						}	
					}															
				}
			}
		}
	}

	private void listenLoopRT()
	{
		int events = 0;
		int waitingtime = 10;
		int res=0;
		Mil1553Packet Msg = new Mil1553Packet();
		TTmkEventData eventData = new TTmkEventData();
		Pointer pBuffer = new Memory(32*2);
		while (threadRunning)
		{
			events = tmkwaitevents(1 << cardNumber, waitingtime);
			if (events == (1 << cardNumber))
			{
				synchronized (syncObject) {
					tmkselect();
					tmkgetevd(eventData);
					if (eventData.nInt == 1)//rtIntCmd
					{
						Msg.commandWord = eventData.union.rt.wCmd; 
						Msg.dataWords[0] = (short) rtgetcmddata(Msg.commandWord & 31);
						Msg.date = LocalDateTime.now();
						Msg.status = EMilPacketStatus.eRECEIVED;	
						
						for (IMilMsgReceivedListener listener: msgReceivedListeners)
						{
							listener.msgReceived(Msg);
						}

					}
					else if (eventData.nInt == 2)//rtIntErr
					{			
						for (DebugReceivedListener listener: DebugReceivedListeners)
						{
							listener.msgReceived("RT has found out a error in the command addressed to it "+String.format("%04X",eventData.union.rt.wStatus));
							listener.msgReceived("Word of a condition RT: "+String.format("%04X",eventData.union.rt.wCmd));
						}
					}
					else if (eventData.nInt == 3)//rtIntData
					{
						Msg.commandWord = eventData.union.rt.wStatus;
						rtdefsubaddr(Mil1553Packet.getRTRBit(Msg.commandWord), Mil1553Packet.getSubAddress(Msg.commandWord));
						int len = Mil1553Packet.getWordsCount(Msg.commandWord);
						if (len==0) len=32;
						rtgetblk(0, pBuffer, len);
						short[] buffer = pBuffer.getShortArray(0,32); 
						System.arraycopy(buffer, 0, Msg.dataWords, 0, 32);
						Msg.date = LocalDateTime.now();
						Msg.status = EMilPacketStatus.eRECEIVED;						
						
						for (IMilMsgReceivedListener listener: msgReceivedListeners)
						{
							listener.msgReceived(Msg);
						}
					}
				}
			}
		}
	}

	private void listenLoopMT()
	{
		int events = 0;
		int waitingtime = 10;
		
		int res=0;
		TTmkEventData eventData = new TTmkEventData();
		int sw;
		Pointer pBuffer = new Memory(64*2);
		while (threadRunning)
		{
			events = tmkwaitevents(1 << cardNumber, waitingtime);
			if (events == (1 << cardNumber))
			{
				synchronized (syncObject) {
					tmkselect();
					tmkgetevd(eventData);


					if (eventData.nInt == 3)
					{

					}
					else if (eventData.nInt == 4)
					{
						while (mtLastBase != eventData.union.mt.wBase)
						{
							res = mtdefbase(mtLastBase);

							sw = mtgetsw();
							int statusword = eventData.union.mt.wResultX;

							mtgetblk(0, pBuffer, 64);
							short[] buffer = pBuffer.getShortArray(0,64); 

					
							Mil1553RawPacketMT rawPacket = new Mil1553RawPacketMT(buffer,sw,statusword);
							Mil1553Packet packet = new Mil1553Packet(rawPacket);
						

			
							for (IMilMsgReceivedListener listener: msgReceivedListeners)
							{
								listener.msgReceived(packet);
							}
							++mtLastBase;
							if (mtLastBase > mtMaxBase)
								mtLastBase = 0;

						}
					}
				}
			}

		}

	}
	public void initAs(MilWorkMode demandWorkMode) throws Eclus1553Exception
	{
		int result = 0;
		synchronized (syncObject) {
			if (initiliased || !workMode.equals(MilWorkMode.eMilWorkModeNotSetted))
				return;

			result =tmkOpen();
			if (result!=0)
				throw new Eclus1553Exception(this,"Ошибка TmkOpen "+result);


			result = tmkconfig();
			if (result!=0)
				throw new Eclus1553Exception(this,"Ошибка tmkconfig "+result);
			result = tmkselect();
			if (result!=0)
				throw new Eclus1553Exception(this,"Ошибка tmkselect "+result);
			if (demandWorkMode.equals(MilWorkMode.eMilWorkModeMT))
			{
				result = mtreset();
				if (result!=0)
					throw new Eclus1553Exception(this,"Ошибка mtreset "+result);

				result|=mtdefirqmode(Elcus1553Device.RT_GENER1_BL|Elcus1553Device.RT_GENER2_BL);
				Assert.assertEquals(0, result);

				mtMaxBase = mtgetmaxbase();
				short i=0;
				for (i = 0; i < mtMaxBase; ++i)
				{
					mtdefbase(i);
					mtdeflink((i + 1), (Elcus1553Device.CX_CONT | Elcus1553Device.CX_NOINT | Elcus1553Device.CX_SIG));
				}
				mtdefbase(i);
				mtdeflink(0, Elcus1553Device.CX_CONT | Elcus1553Device.CX_NOINT | Elcus1553Device.CX_SIG);
				mtstop();
				runnerThread = new Thread(this::listenLoopMT);

				mtstartx(0, (CX_CONT | CX_NOINT | CX_NOSIG));
			}
			else if (demandWorkMode.equals(MilWorkMode.eMilWorkModeRT))
			{
				result = rtreset();
				if (result!=0)
				{
					throw new Eclus1553Exception(this,"Ошибка rtreset "+result);
				}
				result = rtdefaddress(rtAddress);
				if (result!=0)
				{
					throw new Eclus1553Exception(this,"Ошибка rtdefaddress "+result);
				}

				result = rtdefmode(0);
				result|= rtdefirqmode(0);
				rtenable(RT_DISABLE);
				runnerThread = new Thread(this::listenLoopRT);
			}
			else if (demandWorkMode.equals(MilWorkMode.eMilWorkModeBC))
			{
				result = bcreset();
				if (result!=0)
				{
					throw new Eclus1553Exception(this,"Ошибка bcreset() "+result);
				}
				result|=bcdefirqmode(RT_GENER1_BL|RT_GENER2_BL);

				if (result!=0)
				{
					throw new Eclus1553Exception(this,"Ошибка bcdefirqmode() "+result);
				}
				runnerThread = new Thread(this::listenLoopBC);
			}
			else 
			{
				return;
			}
			workMode = demandWorkMode;
			initiliased=true;
			threadRunning = true;
			runnerThread.start();
		}
	}

	public Elcus1553Device(int cardNumber)
	{
		this.cardNumber = cardNumber;
	}
	Integer rtbusy()
	{
		return (CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtbusy,0));
	}
	int rtgetcmddata(int rtBusCommand)
	{
		return (CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtgetcmddata, rtBusCommand));
	}

	int rtenable(int rtEnable)
	{
		return (CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtenable, rtEnable));
	}
	static int tmkOpen()
	{
		int _VTMK4Arg;
		int ErrorCode;

		if (_hVTMK4VxD != 0)
			return 0;

		_hVTMK4VxD = CLibrary.INSTANCE.open("/dev/tmk1553b", 0);
		if (_hVTMK4VxD < 0)
		{
			ErrorCode = _hVTMK4VxD;
			_hVTMK4VxD = 0;
			return ErrorCode;
		}
		if ((_VTMK4Arg = CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCGetVersion, 0)) < 0 || _VTMK4Arg < TMK_VERSION_MIN)
		{
			CLibrary.INSTANCE.close(_hVTMK4VxD);
			_hVTMK4VxD = 0;
			return VTMK_BAD_VERSION;
		}
		return 0;
	}
	int mtgetsw()
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCmtgetsw,0);
	}
	int tmkconfig()
	{
		return tmkconfig(cardNumber);
	}
	private int tmkconfig(int tmkNumber)
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCtmkconfig, tmkNumber);
	}

	int tmkgetmaxn()
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCtmkgetmaxn, 0);
	}

	int tmkdone()
	{
		return tmkdone(cardNumber);
	}
	private int tmkdone(int tmkNumber)
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCtmkdone, tmkNumber);
	}

	int tmkselect()
	{
		return tmkselect(cardNumber);
	}
	private int tmkselect(int tmkNumber)
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCtmkselect, tmkNumber);
	}

	int tmkselected()
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCtmkselected, 0);
	}

	int tmkgetmode()
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCtmkgetmode, 0);
	}

	void tmksetcwbits(int tmkSetControl)
	{
		CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCtmksetcwbits, tmkSetControl);
	}

	void tmkclrcwbits(int tmkClrControl)
	{
		CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCtmksetcwbits, tmkClrControl);
	}

	int tmkgetcwbits()
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCtmkgetcwbits, 0);
	}

	int tmkwaitevents(int maskEvents, int fWait)
	{
		int[] _VTMK4Arg = new int[2];
		_VTMK4Arg[0] = maskEvents;
		_VTMK4Arg[1] = fWait;
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCtmkwaitevents, _VTMK4Arg);
	}

	void tmkgetevd(TTmkEventData pEvD)
	{   

		CC cc = new CC();


		CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCtmkgetevd&0xffffffff, cc.getPointer());
		ByteBuffer bb =  cc.getPointer().getByteBuffer(0, 6*2);
		pEvD.nInt = bb.getInt();
		pEvD.wMode = bb.getShort();
		switch (pEvD.wMode&0xffff)
		{
		case BC_MODE:
			switch (pEvD.nInt)
			{
			case 1:
				pEvD.union.bc.wResult = bb.getShort();
				break;
			case 2:
				pEvD.union.bc.wResult = bb.getShort();
				pEvD.union.bc.wAW1 = bb.getShort();
				pEvD.union.bc.wAW2 = bb.getShort();
				break;
			case 3:
				pEvD.union.bcx.wResultX = bb.getShort();
				pEvD.union.bcx.wBase = bb.getShort();
				break;
			case 4:
				pEvD.union.bcx.wBase = bb.getShort();
				break;
			}
			break;
		case MT_MODE:
			switch (pEvD.nInt)
			{
			case 3:
				pEvD.union.mt.wResultX = bb.getShort();
				pEvD.union.mt.wBase = bb.getShort();
				break;
			case 4:
				pEvD.union.mt.wBase = bb.getShort();
				break;
			}
			break;
		case RT_MODE:
			switch (pEvD.nInt)
			{
			case 1:
				pEvD.union.rt.wCmd = bb.getShort();
				break;
			case 2:
			case 3:
				pEvD.union.rt.wStatus = bb.getShort();
				break;
			}
			break;
		case MRT_MODE:
			pEvD.union.mrt.wStatus = bb.getShort();
			break;
		case UNDEFINED_MODE:
			pEvD.union.tmk.wRequest = bb.getShort();
			break;
		}
	}

	void tmkgetinfo(TTmkConfigData pConfD)
	{
		CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCtmkgetinfo, pConfD.getPointer());
	}

	int bcreset()
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCbcreset, 0);
	}

	int mtreset()
	{
		return (CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCmtreset,0));
	}

	int mtdefirqmode(int mtIrqMode)
	{
		return bcdefirqmode(mtIrqMode);
	}
	int bcdefirqmode(int bcIrqMode)
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCbcdefirqmode, bcIrqMode);
	}

	int bcgetirqmode()
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCbcgetirqmode, 0);
	}

	int mtgetmaxbase()
	{
		return bcgetmaxbase();
	}
	int bcgetmaxbase()
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCbcgetmaxbase, 0);
	}

	int mtdefbase(int mtBasePC)
	{
		return bcdefbase(mtBasePC);
	}
	int bcdefbase(int bcBasePC)
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCbcdefbase&0xffffffff, bcBasePC);
	}

	int bcgetbase()
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCbcgetbase, 0);
	}

	void bcputw(int bcAddr, int bcData)
	{
		CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCbcputw, bcAddr | (bcData << 16));
	}

	int bcgetw(int bcAddr)
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCbcgetw, bcAddr);
	}

	int bcgetansw(int bcCtrlCode)
	{
		int _VTMK4Arg;
		_VTMK4Arg = bcCtrlCode;
		CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCbcgetansw, _VTMK4Arg);
		return _VTMK4Arg;
	}

	void rtgetblk(int rtAddr, Pointer pcBuffer, int cwLength)
	{
		CLong2 c = new CLong2();
		ByteBuffer bb = c.getPointer().getByteBuffer(0,c.size());
		bb.putInt((rtAddr|cwLength<<16));
		bb.putInt(0);
		bb.putLong(Pointer.nativeValue(pcBuffer));
		CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtgetblk, c.getPointer());
	}
	void rtputcmddata(Integer rtBusCommand, Integer rtData)
	{
		CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtputcmddata, rtBusCommand | (rtData << 16));
	}
	void rtputblk(Integer rtAddr, Pointer pcBuffer, Integer cwLength)
	{
		CLong2 c = new CLong2();
		ByteBuffer bb = c.getPointer().getByteBuffer(0,c.size());
		bb.putInt((rtAddr|cwLength<<16));
		bb.putInt(0);
		bb.putLong(Pointer.nativeValue(pcBuffer));
		CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtputblk, c.getPointer());
	}

	void bcputblk(int bcAddr, Pointer pcBuffer, short cwLength)
	{
		CLong2 c = new CLong2();
		ByteBuffer bb = c.getPointer().getByteBuffer(0,c.size());
		bb.putInt((bcAddr|cwLength<<16));
		bb.putInt(0);
		bb.putLong(Pointer.nativeValue(pcBuffer));
		CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCbcputblk&0xffffffff, c.getPointer());
	}
	void mtgetblk(int mtAddr, Pointer pcBuffer, int cwLength)
	{
		bcgetblk(mtAddr, pcBuffer, cwLength);
	}
	void bcgetblk(int bcAddr, Pointer pcBuffer, int cwLength)
	{		
		CLong2 c = new CLong2();
		ByteBuffer bb = c.getPointer().getByteBuffer(0,c.size());
		bb.putInt((bcAddr|cwLength<<16));
		bb.putInt(0);
		bb.putLong(Pointer.nativeValue(pcBuffer));
		CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCbcgetblk&0xffffffff, c.getPointer());
	}

	int bcdefbus(int bcBus)
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCbcdefbus, bcBus);
	}

	int bcgetbus()
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCbcgetbus, 0);
	}

	int bcstart(int bcBase, int bcCtrlCode)
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCbcstart, bcBase | (bcCtrlCode << 16));
	}

	int mtstartx(int mtBase, int mtCtrlCode)
	{
		int res = 0;
		if (paused)
		{
			res= bcstartx(mtBase,mtCtrlCode);
			if (res == 0)
				paused=false;
		}
		return res;
	}
	int bcstartx(int bcBase, int bcCtrlCode)
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCbcstartx, bcBase | (bcCtrlCode << 16));
	}

	int mtdeflink(int mtBase, int mtCtrlCode)
	{
		return bcdeflink(mtBase, mtCtrlCode);
	}
	int bcdeflink(int bcBase, int bcCtrlCode)
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCbcdeflink, bcBase | (bcCtrlCode << 16));
	}

	int bcgetlink() 
	{
		class C extends Structure
		{
			int _VTMK4Arg;
		}
		C c = new C();
		CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCbcgetlink, c.getPointer());
		return c._VTMK4Arg;
	}

	int mtstop()
	{
		int res = 0;
		if (!paused)
		{
			res= bcstop();
			if (res == 0)
				paused=true;
		}
		return res;
		 
	}
	int bcstop()
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCbcstop, 0);
	}

	int bcgetstate()
	{
		class C extends Structure
		{
			int _VTMK4Arg;
		}
		C c = new C();
		CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCbcgetstate, c.getPointer());
		return c._VTMK4Arg;
	}

	int rtreset()
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtreset, 0);
	}

	int rtdefirqmode(int rtIrqMode)
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtdefirqmode, rtIrqMode);
	}

	int rtgetirqmode()
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtgetirqmode,0);
	}

	int rtdefmode(int rtMode)
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtdefmode, rtMode);
	}

	int rtgetmode()
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtgetmode, 0);
	}

	int rtgetmaxpage()
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtgetmaxpage, 0);
	}

	int rtdefpage(int rtPage)
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtdefpage, rtPage);
	}

	int rtgetpage()
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtgetpage, 0);
	}

	int rtdefpagepc(int rtPagePC)
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtdefpagepc, rtPagePC);
	}

	int rtdefpagebus(int rtPageBus)
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtdefpagebus, rtPageBus);
	}

	int rtgetpagepc()
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtgetpagepc, 0);
	}

	int rtgetpagebus()
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtgetpagebus, 0);
	}

	int rtdefaddress(int rtAddress)
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtdefaddress, rtAddress);
	}

	int rtgetaddress()
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtgetaddress, 0);
	}

	void rtdefsubaddr(int rtDir, int rtSubAddr)
	{
		CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtdefsubaddr, rtDir | (rtSubAddr << 16));
	}

	int rtgetsubaddr()
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtgetsubaddr, 0);
	}

	void rtputw(int rtAddr, int rtData)
	{
		CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtputw, rtAddr | (rtData << 16));
	}

	int rtgetw(int rtAddr)
	{
		return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtgetw, rtAddr);
	}

	void rtputblk(int rtAddr, Pointer pcBuffer, short cwLength)
	{
		CLong2 c = new CLong2();
		ByteBuffer bb = c.getPointer().getByteBuffer(0,c.size());
		bb.putInt((rtAddr|cwLength<<16));
		bb.putInt(0);
		bb.putLong(Pointer.nativeValue(pcBuffer));

		CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCrtputblk, c.getPointer());
	}

}
