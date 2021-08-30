/*

impl fmt::Display for DoubleError {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "invalid first item to double")
    }
}

 */

#[derive(Debug, Clone)]
pub struct RuntimeError;

impl fmt::Display for RuntimeError {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "runtime error")
    }
}

#[derive(Debug,Clone)]
pub struct AbortedSnapshotException;

impl fmt::Display for AbortedSnapshotException {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "snapshot aborted")
    }
}
