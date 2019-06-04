// IPackageInstallObserver.aidl
package android.content.pm;

// Declare any non-default types here with import statements

interface IPackageInstallObserver {

    void packageInstalled(String pkgName, int returnCode);
}
