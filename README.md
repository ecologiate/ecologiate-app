# ecologiate-app
App en Android

1) Instalar git:
https://git-scm.com/

2) Bajarse el proyecto:
"git clone git@github.com:ecologiate/ecologiate-app.git"

Si no funciona por permisos, posiblemente tengan que configurar certificados ssh y vincularlos a su cuenta de github con ssh-keygen:
https://dreyacosta.com/comandos-basicos-para-manejarse-con-git-y-github/
no tienen que hacer "git init", eso es si están creando un repo, en este caso ya está creado y se lo tienen que bajar

3) Bajarse el Android Studio. El mismo viene con la jdk de Java y todo lo necesario para correr el proyecto, codear y debugear. Es bastante intuitivo. Se puede correr en el emulador que viene con Android Studio o en un celu (recomendado) por cable usb, hay que activarle las opciones de desarrollador y debug usb.

4) Generar clave para poder authenticarse con Facebook en cada entorno de desarrollo y agregarlo a la app de Facebook. El password default para el keystore es "android".
App Facebook:
https://developers.facebook.com/apps/149676695596008
Generación de hash Mac:
keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore | openssl sha1 -binary | openssl base64
Generación de hash Windows:
keytool -exportcert -alias androiddebugkey -keystore %HOMEPATH%\.android\debug.keystore | openssl sha1 -binary | openssl base64
Generación de hash de release (al subir a la google play store):
keytool -exportcert -alias YOUR_RELEASE_KEY_ALIAS -keystore YOUR_RELEASE_KEY_PATH | openssl sha1 -binary | openssl base64
