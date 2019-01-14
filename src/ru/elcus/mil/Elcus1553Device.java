package ru.elcus.mil;

import com.sun.jna.Structure;
import com.sun.jna.Union;

/**
 * Порт C++ класса для работы с платами (PCI, PCI-E) МКО от Элкус
 * @author qmor
 *
 */
public class Elcus1553Device {
	
	

	static final int TMK_VERSION_MIN =0x0403; /* v4.03 */
	static final int TMK_VERSION =0x0406 ;    /* v4.06 */


	//#ifdef _TMK1553B_MRT
	//static final int MAX_TMK_NUMBER (32-1)
	//#else
	//static final int MAX_TMK_NUMBER (8-1)
	//#endif //def TMK1553B_MRT

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
	static final int RT_GENER1_BL =0x0004;
	static final int RT_GENER2_BL =0x4000;
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

	static final int CX_CONT =0x0010;
	static final int CX_STOP =0x0000;
	static final int CX_BUS_0 =0x0000;
	static final int CX_BUS_A =0x0000;
	static final int CX_BUS_1 =0x0020;
	static final int CX_BUS_B =0x0020;
	static final int CX_NOSIG =0x0000;
	static final int CX_SIG =0x8000;
	static final int CX_INT =0x0000;
	static final int CX_NOINT =0x0020;

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

	static final int  TMK_IOCmrtdefbrcpage  = ioctl._IO(TMK_IOC_MAGIC, VTMK_mrtdefbrcpage+TMK_IOC0);
	static final int  TMK_IOCmrtgetbrcpage  = ioctl._IO(TMK_IOC_MAGIC, VTMK_mrtgetbrcpage+TMK_IOC0);
	
	private class union extends Union
	{
		class __bc extends Structure
	    {
	      short wResult;
	      short wAW1;
	      short wAW2;
	    }
	    
	    class __bcx extends Structure
	    {
	      short wBase;
	      short wResultX;
	    }
	    
	    class __rt  extends Structure
	    {
	      short wStatus;
	      short wCmd;
	    }

	    class __mt  extends Structure
	    {
	      short wBase;
	      short wResultX;
	    };
	    
	    class __mrt  extends Structure
	    {
	      short wStatus;
	    };
	    
	    class __tmk  extends Structure
	    {
	      short wRequest;
	    };
	    
	    __bc bc;
	    __bcx bcx;
	    __rt rt;
	    __mt mt;
	    __mrt mrt;
	    __tmk tmk;
	}
	
	public class __TTmkConfigData extends Structure
	{
		 short nType;
		 byte[] szName = new byte[10];
		 short wPorts1;
		 short wPorts2;
		 short wIrq1;
		 short wIrq2;
		 short wIODelay;
	}
	
	public class __TTmkEventData extends Structure
	{
		int nInt;
		short wMode;
		
		union un;
	}
	
    int TmkOpen()
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
    
    int tmkconfig(int tmkNumber)
    {
        return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCtmkconfig, tmkNumber);
    }
    
    int tmkgetmaxn()
    {
        return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCtmkgetmaxn, 0);
    }
    
    int tmkdone(int tmkNumber)
    {
        return CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCtmkdone, tmkNumber);
    }
    
    int tmkselect(int tmkNumber)
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

    void tmkgetevd(__TTmkEventData pEvD)
    {
        short[] _awVTMK4OutBuf = new short[6]; // !!!!!!!!
        CLibrary.INSTANCE.ioctl(_hVTMK4VxD, TMK_IOCtmkgetevd, _awVTMK4OutBuf);
        pEvD->nInt = ((DWORD*)(_awVTMK4OutBuf))[0];
        switch (pEvD->wMode = _awVTMK4OutBuf[2])
        {
        case BC_MODE:
            switch (pEvD->nInt)
            {
            case 1:
                pEvD->bc.wResult = _awVTMK4OutBuf[3];
                break;
            case 2:
                pEvD->bc.wResult = _awVTMK4OutBuf[3];
                pEvD->bc.wAW1 = _awVTMK4OutBuf[4];
                pEvD->bc.wAW2 = _awVTMK4OutBuf[5];
                break;
            case 3:
                pEvD->bcx.wResultX = _awVTMK4OutBuf[3];
                pEvD->bcx.wBase = _awVTMK4OutBuf[4];
                break;
            case 4:
                pEvD->bcx.wBase = _awVTMK4OutBuf[3];
                break;
            }
            break;
        case MT_MODE:
            switch (pEvD->nInt)
            {
            case 3:
                pEvD->mt.wResultX = _awVTMK4OutBuf[3];
                pEvD->mt.wBase = _awVTMK4OutBuf[4];
                break;
            case 4:
                pEvD->mt.wBase = _awVTMK4OutBuf[3];
                break;
            }
            break;
        case RT_MODE:
            switch (pEvD->nInt)
            {
            case 1:
                pEvD->rt.wCmd = _awVTMK4OutBuf[3];
                break;
            case 2:
            case 3:
                pEvD->rt.wStatus = _awVTMK4OutBuf[3];
                break;
            }
            break;
        case MRT_MODE:
            pEvD->mrt.wStatus = _awVTMK4OutBuf[3];
            break;
        case UNDEFINED_MODE:
            pEvD->tmk.wRequest = _awVTMK4OutBuf[3];
            break;
        }
    }
    
    void tmkgetinfo(TTmkConfigData *pConfD)
    {
    	ioctl(_hVTMK4VxD, TMK_IOCtmkgetinfo, pConfD);
    }

    int bcreset(void)
    {
        return (
#ifdef USE_TMK_ERROR
                   tmkError =
#endif
                       ioctl(_hVTMK4VxD, TMK_IOCbcreset));
    }


    int bcdefirqmode(TMK_DATA bcIrqMode)
    {
        return (
#ifdef USE_TMK_ERROR
                   tmkError =
#endif
                       ioctl(_hVTMK4VxD, TMK_IOCbcdefirqmode, bcIrqMode));
    }

    TMK_DATA_RET bcgetirqmode(void)
    {
#ifdef USE_TMK_ERROR
        tmkError = 0;
#endif
        return (ioctl(_hVTMK4VxD, TMK_IOCbcgetirqmode));
    }
}
