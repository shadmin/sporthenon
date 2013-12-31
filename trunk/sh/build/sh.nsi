;-------------------------;
; Sporthenon Setup Script ;
;-------------------------;

!define VERSION "0.5.1"
!define OUTPUT "C:\Docs\Sporthenon\Setup"
!define IMGPATH "C:\Docs\Sporthenon\Setup\Graphics"
!define EXEPATH "C:\Docs\Sporthenon\Workspace\Sporthenon\build"
!define LIBPATH "C:\Docs\Sporthenon\Workspace\Sporthenon\WebContent\WEB-INF\lib"

; General attributes
SetCompressor bzip2
SetOverwrite off
Name "Sporthenon"
OutFile "${OUTPUT}\Sporthenon-${VERSION}-setup.exe"
RequestExecutionLevel admin
InstallDir "$PROGRAMFILES\Sporthenon"
;ShowInstDetails show
;ShowUninstDetails show

!include "MUI.nsh"
!include "Sections.nsh"

!define MUI_ICON "${IMGPATH}\install.ico"
!define MUI_UNICON "${IMGPATH}\uninstall.ico"

; Pages
!define MUI_HEADERIMAGE
!define MUI_WELCOMEPAGE_TITLE "Welcome to Sporthenon Setup Wizard"
!define MUI_WELCOMEPAGE_TEXT "\r\nThis wizard will guide you through the installation of 'Sporthenon' on your computer.\r\n\r\nClick 'Next' to continue."
!define MUI_COMPONENTSPAGE_SMALLDESC
!insertmacro MUI_PAGE_WELCOME
;!insertmacro MUI_PAGE_LICENSE "License_FR.txt"
!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

!define MUI_ABORTWARNING

; Langue
!insertmacro MUI_LANGUAGE "English"
!insertmacro MUI_RESERVEFILE_LANGDLL

; Sections
Section "Sporthenon Update" Main
  SectionIn RO
  SetShellVarContext all

  SetOutPath "$INSTDIR"
  File "${EXEPATH}\shupdate.exe"
  File "${EXEPATH}\shupdate.lap"
  File "${EXEPATH}\shupdate.jar"
  
  CreateDirectory "$APPDATA\Sporthenon"
  ;SetOutPath "$APPDATA\Sporthenon"
  ;File "${EXEPATH}\classes\com\sporthenon\updater\options.swing.xml"
  ;Rename "$APPDATA\Sporthenon\options.swing.xml" "$APPDATA\Sporthenon\options.xml"

  SetOutPath "$INSTDIR\Lib"
  File "${LIBPATH}\*.jar"

  WriteUninstaller "$INSTDIR\Uninstall.exe"
  WriteRegStr HKCU "Software\Sporthenon" "InstallDir" $INSTDIR
  WriteRegStr HKCU "Software\Sporthenon" "ConfigDir" $APPDATA\Sporthenon
SectionEnd

Section "Shortcut (Desktop)" Shortcut1
  SetOutPath "$DESKTOP"
  CreateShortCut "Sporthenon Update.lnk" "$INSTDIR\shupdate.exe" "" "" "" "SW_SHOWNORMAL" "" ""
SectionEnd

Section "Shortcut (Start Menu)" Shortcut2
  CreateDirectory "$STARTMENU\Programs\Sporthenon"
  SetOutPath "$STARTMENU\Programs\Sporthenon"
  CreateShortCut "Sporthenon Update.lnk" "$INSTDIR\shupdate.exe" "" "" "" "SW_SHOWNORMAL" "" ""
  CreateShortCut "Uninstall.lnk" "$INSTDIR\Uninstall.exe" "" "" "" "SW_SHOWNORMAL" "" ""
SectionEnd
  
Section "Uninstall"
  SetShellVarContext all
  RMDir /r "$INSTDIR\Lib"
  RMDir /r "$INSTDIR"
  RMDir /r "$APPDATA\Sporthenon"
  RMDir /r "$STARTMENU\Programs\Sporthenon"
  Delete "$DESKTOP\Sporthenon Update.lnk"
  DeleteRegKey /ifempty HKCU "Software\Sporthenon"
SectionEnd