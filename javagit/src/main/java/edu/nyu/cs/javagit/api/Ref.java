package edu.nyu.cs.javagit.api;

import edu.nyu.cs.javagit.utilities.CheckUtilities;

/**
 * This class represents the name of a commit. The types of <code>CommitName</code>s, specified
 * in the <code>CommitNameType</code> enumeration, are:
 * 
 * <ul>
 * <li>HEAD</li>
 * <li>SHA1</li>
 * </ul>
 * 
 * If the <code>CommitNameType</code> of a <code>CommitName</code> instance is HEAD, then a
 * commit offset value is maintained. This commit offset indicates the commit back from the current
 * HEAD commit that the instance represents. Values greater than or equal to zero are acceptable and
 * correspond to:
 * 
 * <ul>
 * <li> 0 - The current HEAD commit</li>
 * <li> 1 - The commit prior to the current HEAD commit</li>
 * <li> 2 - The second commit prior to the current HEAD commit</li>
 * <li> etc...</li>
 * </ul>
 * 
 * If the <code>CommitNameType</code> of a <code>CommitName</code> instance is SHA1, then the
 * SHA1 value of the commit is represented by a <code>String</code> value. This string value can
 * be
 * 
 * <ul>
 * <li> the full SHA1</li>
 * <li> a SHA1 truncated to the first n characters</li>
 * </ul>
 */
public class Ref {

  /**
   * An enumeration of the types of commit names.
   */
  public static enum CommitNameType {
    HEAD, SHA1
  }

  /** The HEAD commit. */
  public static final Ref HEAD;

  /** The prior HEAD commit. */
  public static final Ref HEAD_1;

  static {
    HEAD = new Ref();
    HEAD.commitNameType = CommitNameType.HEAD;
    HEAD.headOffset = 0;

    HEAD_1 = new Ref();
    HEAD_1.commitNameType = CommitNameType.HEAD;
    HEAD_1.headOffset = 1;
  }

  // The type of this commit name.
  private Ref.CommitNameType commitNameType;

  // If the commit type is HEAD, this is the number of commits back from the head of the commit.
  private int headOffset = -1;

  // If the commit type is SHA1, this is the name of the commit.
  private String sha1Name = null;

  /**
   * Creates a <code>CommitName</code> of type <code>HEAD</code>.
   * 
   * @param headOffset
   *          The offset of the commit back from the HEAD commit. If the value is less than zero, an
   *          <code>IllegalArgumentException</code> is thrown.
   * @return A <code>CommitName</code> instance of type <code>HEAD</code>.
   */
  public static Ref createHeadCommitName(int headOffset) {
    CheckUtilities.checkIntArgumentGreaterThan(headOffset, -1, "headOffset");

    if (0 == headOffset) {
      return HEAD;
    } else if (1 == headOffset) {
      return HEAD_1;
    }

    Ref cn = new Ref();
    cn.commitNameType = CommitNameType.HEAD;
    cn.headOffset = headOffset;
    return cn;
  }

  /**
   * Creates a <code>CommitName</code> of type <code>SHA1</code>.
   * 
   * @param sha1Name
   *          The SHA1 name of this commit. The value can be a short name or the full SHA1 value. If
   *          the value is null, a <code>NullPointerException</code> is thrown. If the value has
   *          length zero, an <code>IllegalArgumentException</code> is thrown.
   * @return A <code>CommitName</code> instance of type <code>SHA1</code>.
   */
  public static Ref createSha1CommitName(String sha1Name) {
    CheckUtilities.checkStringArgument(sha1Name, "sha1Name");

    Ref cn = new Ref();
    cn.commitNameType = CommitNameType.SHA1;
    cn.sha1Name = sha1Name;
    return cn;
  }

  /**
   * Gets the type of the <code>CommitName</code> instance.
   * 
   * @return The type of the <code>CommitName<code> instance.
   */
  public Ref.CommitNameType getCommitNameType() {
    return commitNameType;
  }

  /**
   * Gets the offset of the commit back from the HEAD commit for this <code>CommitName</code>
   * instance.
   * 
   * @return If the type of this <code>CommitName</code> is not <code>HEAD</code>, then -1 is
   *         returned. Otherwise, the offset of the commit back from the current HEAD commit is
   *         returned.
   */
  public int getHeadOffset() {
    return headOffset;
  }

  /**
   * Gets the SHA1 name of this commit.
   * 
   * @return If the type of this <code>CommitName</code> is not <code>SHA1</code>, then null is
   *         returned. Otherwise, the name of this commit is returned.
   */
  public String getSha1Name() {
    return sha1Name;
  }

  public String toString() {
    if (CommitNameType.HEAD == commitNameType) {
      if (0 == headOffset) {
        return "HEAD";
      } else if (1 == headOffset) {
        return "HEAD^1";
      } else {
        return "HEAD~" + Integer.toString(headOffset);
      }
    } else if (CommitNameType.SHA1 == commitNameType) {
      return sha1Name;
    } else {
      return "";
    }
  }

  public boolean equals(Object o) {
    if (!(o instanceof Ref)) {
      return false;
    }

    Ref cn = (Ref) o;

    if (!CheckUtilities.checkObjectsEqual(commitNameType, cn.getCommitNameType())) {
      return false;
    }
    if (!CheckUtilities.checkObjectsEqual(sha1Name, cn.getSha1Name())) {
      return false;
    }
    if (cn.getHeadOffset() != headOffset) {
      return false;
    }

    return true;
  }

  public int hashCode() {
    int ret = commitNameType.hashCode() + headOffset;
    return (null == sha1Name) ? ret : ret + sha1Name.hashCode();
  }

}