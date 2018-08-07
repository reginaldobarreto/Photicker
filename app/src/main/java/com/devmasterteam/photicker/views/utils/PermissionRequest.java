package com.devmasterteam.photicker.views.utils;

/*
Criar uma classe com um metodo publico estatico que retorna true,
com os seguintes parametros:(int requestCode, Activity activity, String[] permissions)
Objetivos:
1º checar a versao SDK se é maior que 23
2º criar uma lista de permissao
3º varrer permissoes liberadas checando permissoes requeridas e adicionar as pendentes na lista
3ºA checar as permissoes requeridas pela activity usando ActivityCompat.checkSelfPermission==PackageManager.PERMISSION_GRANTED armazenar em boolean
3ºB adicionar permissoes pendentes (checar variavel boolean) na lista de permissao
4º apos a varredura checar se a lista vazia retorne true
5º lista preenchida solicitar permissao via ActivityCompat
6º no main no metodo onRequestPermissionsResult
6ºA varrer grantResults
6ºB verificar permissoes negadas
7º criar metodo para alertar ao usuario da necessidade de aceitar as permissoes
 */

public class PermissionRequest {


}
