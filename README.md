# Proyecto de ejemplo Android Cortafilas para Onepay

## Descripción

Este proyecto busca demostrar la integración de Onepay en modalidad Cortafilas, [descrita en la documentación oficial.](https://www.transbankdevelopers.cl/documentacion/onepay#integracion-cortafila)

## Descarga desde Google Play

Si estás buscando la aplicación para descargarlo, puedes hacerlo directamente desde [Google Play.]( 
https://play.google.com/store/apps/details?id=cl.transbank.onepay.pos)

## Uso de aplicación

Después de descargar la aplicación desde Google Play en un teléfono o tablet Android, debes iniciar la aplicación con el nombre `Demo Cortafilas Onepay`. Luego de eso, selecciona uno o múltiples productos, presiona el botón `Pagar`, y verás un diálogo de pago de Onepay. 

Escanea el código QR utilizando la aplicación oficial Onepay, autoriza el pago, y verás el resultado del pago en el teléfono desde donde estás pagando, y en la aplicación Cortafilas.

## Requisitos para ambiente de desarrollo

- Android Studio
- [Aplicación backend ejecutándose](https://github.com/TransbankDevelopers/transbank-demo-cortafilas-backend-onepay), que crea y confirma la transacción. (En caso que quieras prescindir de esto, puedes ocupar la que ocupamos actualmente: https://cortafilas-onepay.herokuapp.com

## Instrucciones para ejecución en ambiente de desarrollo

- Debes indicarle a la aplicación Android la ruta en donde está alojada la aplicación backend. Para esto, debes modificar el archivo `HTTPClient.java` en `app/src/main/java/cl/transbank/onepay/pos/utils`. Debes modificar la variable `BASE_URL`, para apuntar al dominio o IP correspondiente.

- Por otro lado, es necesario configurar Firebase para poder recepcionar notificaciones push con el estado de la transacción. Para ello, debes dirigirte a la [Consola de Firebase](https://console.firebase.google.com), crear un proyecto, para luego ir a Project Overview y Configuración del Proyecto. Ve a `Añade Firebase a tu aplicación de Android`, ingresa el nombre del paquete (por defecto es `cl.transbank.onepay.pos`, descarga el archivo `google-services.json`, y cópialo en la carpeta /app de la aplicación.

- ¡Compila y ejecuta la aplicación a través de Android Studio!
